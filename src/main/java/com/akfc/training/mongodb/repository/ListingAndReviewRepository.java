package com.akfc.training.mongodb.repository;

import com.akfc.training.mongodb.model.ListingAndReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ListingAndReviewRepository extends MongoRepository<ListingAndReview, String> {
    
    // Find by property type
    List<ListingAndReview> findByPropertyType(String propertyType);
    
    // Find by room type
    List<ListingAndReview> findByRoomType(String roomType);
    
    // Find by host name
    @Query("{'host.host_name': ?0}")
    List<ListingAndReview> findByHostName(String hostName);
    
    // Find by accommodates
    List<ListingAndReview> findByAccommodates(Integer accommodates);
    
    // Find by price range
    List<ListingAndReview> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find by number of bedrooms
    List<ListingAndReview> findByBedrooms(Integer bedrooms);
    
    // Find by location (city/market)
    @Query("{'address.market': ?0}")
    List<ListingAndReview> findByMarket(String market);
    
    // Find by country
    @Query("{'address.country': ?0}")
    List<ListingAndReview> findByCountry(String country);
    
    // Find available listings (with availability > 0)
    @Query("{'availability.availability_30': {$gt: 0}}")
    List<ListingAndReview> findAvailableListings();
    
    // Find superhost listings
    @Query("{'host.host_is_superhost': true}")
    List<ListingAndReview> findSuperhostListings();
    
    // Find by minimum nights
    List<ListingAndReview> findByMinimumNights(String minimumNights);
    
    // Find listings with reviews
    @Query("{'number_of_reviews': {$gt: 0}}")
    Page<ListingAndReview> findListingsWithReviews(Pageable pageable);
    
    // Custom query to find by multiple criteria
    @Query("{'property_type': ?0, 'accommodates': {$gte: ?1}, 'price': {$lte: ?2}}")
    List<ListingAndReview> findByPropertyTypeAndAccommodatesAndMaxPrice(
            String propertyType, Integer minAccommodates, BigDecimal maxPrice);
}