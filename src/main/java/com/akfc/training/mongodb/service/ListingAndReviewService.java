package com.akfc.training.mongodb.service;

import com.akfc.training.mongodb.model.ListingAndReview;
import com.akfc.training.mongodb.repository.ListingAndReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAndReviewService {
    
    private final ListingAndReviewRepository repository;
    private final MongoTemplate mongoTemplate;
    
    // ========== CRUD Operations using MongoRepository ==========
    
    public List<ListingAndReview> findAll() {
        log.info("Finding all listings using repository");
        return repository.findAll();
    }
    
    public Page<ListingAndReview> findAllPaginated(int page, int size) {
        log.info("Finding all listings with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
    
    public Optional<ListingAndReview> findById(String id) {
        log.info("Finding listing by id: {}", id);
        return repository.findById(id);
    }
    
    public ListingAndReview save(ListingAndReview listing) {
        log.info("Saving listing: {}", listing.getName());
        return repository.save(listing);
    }
    
    public void deleteById(String id) {
        log.info("Deleting listing by id: {}", id);
        repository.deleteById(id);
    }
    
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
    
    // ========== Repository-based Query Methods ==========
    
    public List<ListingAndReview> findByPropertyType(String propertyType) {
        log.info("Finding listings by property type: {}", propertyType);
        return repository.findByPropertyType(propertyType);
    }
    
    public List<ListingAndReview> findByRoomType(String roomType) {
        log.info("Finding listings by room type: {}", roomType);
        return repository.findByRoomType(roomType);
    }
    
    public List<ListingAndReview> findByHostName(String hostName) {
        log.info("Finding listings by host name: {}", hostName);
        return repository.findByHostName(hostName);
    }
    
    public List<ListingAndReview> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Finding listings by price range: {} - {}", minPrice, maxPrice);
        return repository.findByPriceBetween(minPrice, maxPrice);
    }
    
    public List<ListingAndReview> findSuperhostListings() {
        log.info("Finding superhost listings");
        return repository.findSuperhostListings();
    }
    
    // ========== MongoTemplate-based Operations ==========
    
    public List<ListingAndReview> findByCustomCriteria(String propertyType, Integer minAccommodates, 
                                                      BigDecimal maxPrice, String country) {
        log.info("Finding listings with custom criteria using MongoTemplate");
        
        Query query = new Query();
        
        if (propertyType != null) {
            query.addCriteria(Criteria.where("property_type").is(propertyType));
        }
        if (minAccommodates != null) {
            query.addCriteria(Criteria.where("accommodates").gte(minAccommodates));
        }
        if (maxPrice != null) {
            query.addCriteria(Criteria.where("price").lte(maxPrice));
        }
        if (country != null) {
            query.addCriteria(Criteria.where("address.country").is(country));
        }
        
        query.with(Sort.by(Sort.Direction.DESC, "price"));
        
        return mongoTemplate.find(query, ListingAndReview.class);
    }
    
    public List<ListingAndReview> findNearLocation(double longitude, double latitude, double maxDistance) {
        log.info("Finding listings near location: {}, {} within {} meters", longitude, latitude, maxDistance);
        
        Query query = new Query();
        query.addCriteria(Criteria.where("address.location.coordinates")
                .nearSphere(new Point(longitude, latitude))
                .maxDistance(maxDistance / 6371000)); // Convert meters to radians
        
        return mongoTemplate.find(query, ListingAndReview.class);
    }
    
    public long updatePriceByPropertyType(String propertyType, BigDecimal newPrice) {
        log.info("Updating price for property type: {} to {}", propertyType, newPrice);
        
        Query query = new Query(Criteria.where("property_type").is(propertyType));
        Update update = new Update().set("price", newPrice);
        
        return mongoTemplate.updateMulti(query, update, ListingAndReview.class).getModifiedCount();
    }
    
    public long updateHostResponseTime(String hostId, String responseTime) {
        log.info("Updating host response time for host: {} to {}", hostId, responseTime);
        
        Query query = new Query(Criteria.where("host.host_id").is(hostId));
        Update update = new Update().set("host.host_response_time", responseTime);
        
        return mongoTemplate.updateMulti(query, update, ListingAndReview.class).getModifiedCount();
    }
    
    // ========== Aggregation Operations using MongoTemplate ==========
    
    public List<PropertyTypeStats> getPropertyTypeStatistics() {
        log.info("Getting property type statistics using aggregation");
        
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("property_type")
                        .count().as("count")
                        .avg("price").as("averagePrice")
                        .min("price").as("minPrice")
                        .max("price").as("maxPrice"),
                Aggregation.sort(Sort.Direction.DESC, "count")
        );
        
        AggregationResults<PropertyTypeStats> results = mongoTemplate.aggregate(
                aggregation, "listingsAndReviews", PropertyTypeStats.class);
        
        return results.getMappedResults();
    }
    
    public List<HostStats> getTopHostsByListings(int limit) {
        log.info("Getting top hosts by number of listings: limit={}", limit);
        
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("host.host_id", "host.host_name")
                        .count().as("listingCount")
                        .avg("review_scores.review_scores_rating").as("averageRating"),
                Aggregation.sort(Sort.Direction.DESC, "listingCount"),
                Aggregation.limit(limit)
        );
        
        AggregationResults<HostStats> results = mongoTemplate.aggregate(
                aggregation, "listingsAndReviews", HostStats.class);
        
        return results.getMappedResults();
    }
    
    public List<ListingAndReview> searchByText(String searchText) {
        log.info("Performing text search: {}", searchText);
        
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(searchText, "i")
                .orOperator(
                        Criteria.where("description").regex(searchText, "i"),
                        Criteria.where("neighborhood_overview").regex(searchText, "i")
                ));
        
        return mongoTemplate.find(query, ListingAndReview.class);
    }
    
    // ========== Bulk Operations ==========
    
    public void saveAll(List<ListingAndReview> listings) {
        log.info("Saving {} listings in bulk", listings.size());
        repository.saveAll(listings);
    }
    
    public long deleteByPropertyType(String propertyType) {
        log.info("Deleting all listings with property type: {}", propertyType);
        
        Query query = new Query(Criteria.where("property_type").is(propertyType));
        return mongoTemplate.remove(query, ListingAndReview.class).getDeletedCount();
    }
    
    // ========== Helper Classes for Aggregation Results ==========
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class PropertyTypeStats {
        private String id; // This will be the property_type
        private long count;
        private BigDecimal averagePrice;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
    }
    
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class HostStats {
        private String hostId;
        private String hostName;
        private long listingCount;
        private Double averageRating;
    }
}