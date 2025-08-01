package com.akfc.training.mongodb.controller;

import com.akfc.training.mongodb.model.ListingAndReview;
import com.akfc.training.mongodb.service.ListingAndReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ListingAndReviewController {
    
    private final ListingAndReviewService service;
    
    // ========== Basic CRUD Operations ==========
    
    @GetMapping
    public ResponseEntity<List<ListingAndReview>> getAllListings() {
        log.info("GET /api/listings - Getting all listings");
        List<ListingAndReview> listings = service.findAll();
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/paginated")
    public ResponseEntity<Page<ListingAndReview>> getAllListingsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/listings/paginated - Getting paginated listings: page={}, size={}", page, size);
        Page<ListingAndReview> listings = service.findAllPaginated(page, size);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ListingAndReview> getListingById(@PathVariable String id) {
        log.info("GET /api/listings/{} - Getting listing by id", id);
        Optional<ListingAndReview> listing = service.findById(id);
        return listing.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ListingAndReview> createListing(@Valid @RequestBody ListingAndReview listing) {
        log.info("POST /api/listings - Creating new listing: {}", listing.getName());
        ListingAndReview savedListing = service.save(listing);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedListing);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ListingAndReview> updateListing(
            @PathVariable String id, 
            @Valid @RequestBody ListingAndReview listing) {
        log.info("PUT /api/listings/{} - Updating listing", id);
        
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        listing.setId(id);
        ListingAndReview updatedListing = service.save(listing);
        return ResponseEntity.ok(updatedListing);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable String id) {
        log.info("DELETE /api/listings/{} - Deleting listing", id);
        
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // ========== Search Operations ==========
    
    @GetMapping("/search/property-type/{propertyType}")
    public ResponseEntity<List<ListingAndReview>> getListingsByPropertyType(
            @PathVariable String propertyType) {
        log.info("GET /api/listings/search/property-type/{} - Finding by property type", propertyType);
        List<ListingAndReview> listings = service.findByPropertyType(propertyType);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/search/room-type/{roomType}")
    public ResponseEntity<List<ListingAndReview>> getListingsByRoomType(
            @PathVariable String roomType) {
        log.info("GET /api/listings/search/room-type/{} - Finding by room type", roomType);
        List<ListingAndReview> listings = service.findByRoomType(roomType);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/search/host/{hostName}")
    public ResponseEntity<List<ListingAndReview>> getListingsByHostName(
            @PathVariable String hostName) {
        log.info("GET /api/listings/search/host/{} - Finding by host name", hostName);
        List<ListingAndReview> listings = service.findByHostName(hostName);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/search/price-range")
    public ResponseEntity<List<ListingAndReview>> getListingsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("GET /api/listings/search/price-range - Finding by price range: {} to {}", minPrice, maxPrice);
        List<ListingAndReview> listings = service.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/search/superhosts")
    public ResponseEntity<List<ListingAndReview>> getSuperhostListings() {
        log.info("GET /api/listings/search/superhosts - Finding superhost listings");
        List<ListingAndReview> listings = service.findSuperhostListings();
        return ResponseEntity.ok(listings);
    }
    
    // ========== Advanced Search with MongoTemplate ==========
    
    @GetMapping("/search/custom")
    public ResponseEntity<List<ListingAndReview>> searchListingsCustom(
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer minAccommodates,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String country) {
        log.info("GET /api/listings/search/custom - Custom search with criteria");
        List<ListingAndReview> listings = service.findByCustomCriteria(
                propertyType, minAccommodates, maxPrice, country);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/search/near")
    public ResponseEntity<List<ListingAndReview>> getListingsNearLocation(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam(defaultValue = "1000") double maxDistance) {
        log.info("GET /api/listings/search/near - Finding listings near location: {}, {}", longitude, latitude);
        List<ListingAndReview> listings = service.findNearLocation(longitude, latitude, maxDistance);
        return ResponseEntity.ok(listings);
    }
    
    @GetMapping("/search/text")
    public ResponseEntity<List<ListingAndReview>> searchListingsByText(
            @RequestParam String searchText) {
        log.info("GET /api/listings/search/text - Text search: {}", searchText);
        List<ListingAndReview> listings = service.searchByText(searchText);
        return ResponseEntity.ok(listings);
    }
    
    // ========== Bulk Operations ==========
    
    @PostMapping("/bulk")
    public ResponseEntity<String> createListingsBulk(@RequestBody List<ListingAndReview> listings) {
        log.info("POST /api/listings/bulk - Creating {} listings in bulk", listings.size());
        service.saveAll(listings);
        return ResponseEntity.status(HttpStatus.CREATED)
                           .body("Successfully created " + listings.size() + " listings");
    }
    
    @DeleteMapping("/property-type/{propertyType}")
    public ResponseEntity<String> deleteListingsByPropertyType(@PathVariable String propertyType) {
        log.info("DELETE /api/listings/property-type/{} - Deleting by property type", propertyType);
        long deletedCount = service.deleteByPropertyType(propertyType);
        return ResponseEntity.ok("Deleted " + deletedCount + " listings");
    }
    
    // ========== Update Operations ==========
    
    @PatchMapping("/price/property-type/{propertyType}")
    public ResponseEntity<String> updatePriceByPropertyType(
            @PathVariable String propertyType,
            @RequestParam BigDecimal newPrice) {
        log.info("PATCH /api/listings/price/property-type/{} - Updating price to {}", propertyType, newPrice);
        long updatedCount = service.updatePriceByPropertyType(propertyType, newPrice);
        return ResponseEntity.ok("Updated price for " + updatedCount + " listings");
    }
    
    @PatchMapping("/host/{hostId}/response-time")
    public ResponseEntity<String> updateHostResponseTime(
            @PathVariable String hostId,
            @RequestParam String responseTime) {
        log.info("PATCH /api/listings/host/{}/response-time - Updating response time to {}", hostId, responseTime);
        long updatedCount = service.updateHostResponseTime(hostId, responseTime);
        return ResponseEntity.ok("Updated response time for " + updatedCount + " listings");
    }
    
    // ========== Analytics and Statistics ==========
    
    @GetMapping("/stats/property-types")
    public ResponseEntity<List<ListingAndReviewService.PropertyTypeStats>> getPropertyTypeStatistics() {
        log.info("GET /api/listings/stats/property-types - Getting property type statistics");
        List<ListingAndReviewService.PropertyTypeStats> stats = service.getPropertyTypeStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/top-hosts")
    public ResponseEntity<List<ListingAndReviewService.HostStats>> getTopHostsByListings(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/listings/stats/top-hosts - Getting top hosts: limit={}", limit);
        List<ListingAndReviewService.HostStats> stats = service.getTopHostsByListings(limit);
        return ResponseEntity.ok(stats);
    }
    
    // ========== Health Check ==========
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Listings API is running");
    }
}