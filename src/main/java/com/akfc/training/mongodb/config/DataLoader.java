package com.akfc.training.mongodb.config;

import com.akfc.training.mongodb.model.*;
import com.akfc.training.mongodb.service.ListingAndReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    
    private final ListingAndReviewService service;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (service.findAll().isEmpty()) {
            log.info("Loading sample data...");
            loadSampleData();
            log.info("Sample data loaded successfully!");
        } else {
            log.info("Data already exists, skipping sample data loading");
        }
    }
    
    private void loadSampleData() {
        List<ListingAndReview> sampleListings = Arrays.asList(
                createSampleListing1(),
                createSampleListing2(),
                createSampleListing3()
        );
        
        service.saveAll(sampleListings);
    }
    
    private ListingAndReview createSampleListing1() {
        return ListingAndReview.builder()
                .id("sample001")
                .name("Cozy Manhattan Apartment")
                .listingUrl("https://www.airbnb.com/rooms/sample001")
                .summary("Beautiful apartment in the heart of Manhattan")
                .description("A wonderful place to stay in NYC with all amenities")
                .neighborhoodOverview("Great neighborhood with restaurants and shops")
                .propertyType("Apartment")
                .roomType("Entire home/apt")
                .bedType("Real Bed")
                .accommodates(4)
                .bedrooms(2)
                .beds(2)
                .bathrooms(new BigDecimal("2.0"))
                .price(new BigDecimal("150.00"))
                .cleaningFee(new BigDecimal("50.00"))
                .securityDeposit(new BigDecimal("100.00"))
                .minimumNights("2")
                .maximumNights("30")
                .numberOfReviews(25)
                .lastScraped(LocalDateTime.now())
                .amenities(Arrays.asList("Wifi", "Kitchen", "Air conditioning", "Heating"))
                .host(createSampleHost1())
                .address(createSampleAddress1())
                .availability(createSampleAvailability())
                .reviewScores(createSampleReviewScores())
                .reviews(Arrays.asList(createSampleReview1()))
                .build();
    }
    
    private ListingAndReview createSampleListing2() {
        return ListingAndReview.builder()
                .id("sample002")
                .name("Brooklyn Loft Studio")
                .listingUrl("https://www.airbnb.com/rooms/sample002")
                .summary("Modern loft in trendy Brooklyn")
                .description("Spacious loft with industrial design and modern amenities")
                .neighborhoodOverview("Hip Brooklyn neighborhood with cafes and galleries")
                .propertyType("Loft")
                .roomType("Entire home/apt")
                .bedType("Real Bed")
                .accommodates(2)
                .bedrooms(1)
                .beds(1)
                .bathrooms(new BigDecimal("1.0"))
                .price(new BigDecimal("120.00"))
                .cleaningFee(new BigDecimal("40.00"))
                .securityDeposit(new BigDecimal("0.00"))
                .minimumNights("3")
                .maximumNights("90")
                .numberOfReviews(15)
                .lastScraped(LocalDateTime.now())
                .amenities(Arrays.asList("Wifi", "Kitchen", "Workspace"))
                .host(createSampleHost2())
                .address(createSampleAddress2())
                .availability(createSampleAvailability())
                .reviewScores(createSampleReviewScores())
                .reviews(Arrays.asList(createSampleReview2()))
                .build();
    }
    
    private ListingAndReview createSampleListing3() {
        return ListingAndReview.builder()
                .id("sample003")
                .name("Queens Family House")
                .listingUrl("https://www.airbnb.com/rooms/sample003")
                .summary("Spacious family house in Queens")
                .description("Perfect for families visiting NYC, quiet neighborhood")
                .neighborhoodOverview("Family-friendly area with parks and schools")
                .propertyType("House")
                .roomType("Entire home/apt")
                .bedType("Real Bed")
                .accommodates(6)
                .bedrooms(3)
                .beds(3)
                .bathrooms(new BigDecimal("2.5"))
                .price(new BigDecimal("200.00"))
                .cleaningFee(new BigDecimal("75.00"))
                .securityDeposit(new BigDecimal("200.00"))
                .minimumNights("5")
                .maximumNights("365")
                .numberOfReviews(40)
                .lastScraped(LocalDateTime.now())
                .amenities(Arrays.asList("Wifi", "Kitchen", "Free parking", "Washer", "Dryer"))
                .host(createSampleHost3())
                .address(createSampleAddress3())
                .availability(createSampleAvailability())
                .reviewScores(createSampleReviewScores())
                .reviews(Arrays.asList(createSampleReview3()))
                .build();
    }
    
    private Host createSampleHost1() {
        return Host.builder()
                .hostId("host001")
                .hostName("John Doe")
                .hostLocation("New York, NY")
                .hostAbout("Love hosting travelers from around the world!")
                .hostResponseTime("within an hour")
                .hostResponseRate(95)
                .hostIsSuperhost(true)
                .hostHasProfilePic(true)
                .hostIdentityVerified(true)
                .hostListingsCount(3)
                .hostTotalListingsCount(3)
                .hostVerifications(Arrays.asList("email", "phone", "reviews"))
                .build();
    }
    
    private Host createSampleHost2() {
        return Host.builder()
                .hostId("host002")
                .hostName("Jane Smith")
                .hostLocation("Brooklyn, NY")
                .hostAbout("Artist and designer who loves sharing my space")
                .hostResponseTime("within a few hours")
                .hostResponseRate(88)
                .hostIsSuperhost(false)
                .hostHasProfilePic(true)
                .hostIdentityVerified(true)
                .hostListingsCount(1)
                .hostTotalListingsCount(1)
                .hostVerifications(Arrays.asList("email", "phone"))
                .build();
    }
    
    private Host createSampleHost3() {
        return Host.builder()
                .hostId("host003")
                .hostName("The Johnson Family")
                .hostLocation("Queens, NY")
                .hostAbout("Family with kids who understand the needs of traveling families")
                .hostResponseTime("within a day")
                .hostResponseRate(92)
                .hostIsSuperhost(true)
                .hostHasProfilePic(true)
                .hostIdentityVerified(true)
                .hostListingsCount(2)
                .hostTotalListingsCount(2)
                .hostVerifications(Arrays.asList("email", "phone", "reviews", "government_id"))
                .build();
    }
    
    private Address createSampleAddress1() {
        Address.Location location = Address.Location.builder()
                .type("Point")
                .coordinates(new Double[]{-73.9857, 40.7484})
                .isLocationExact(false)
                .build();
                
        return Address.builder()
                .street("Manhattan, NY, United States")
                .suburb("Manhattan")
                .governmentArea("Midtown")
                .market("New York")
                .country("United States")
                .countryCode("US")
                .location(location)
                .build();
    }
    
    private Address createSampleAddress2() {
        Address.Location location = Address.Location.builder()
                .type("Point")
                .coordinates(new Double[]{-73.9442, 40.6782})
                .isLocationExact(false)
                .build();
                
        return Address.builder()
                .street("Brooklyn, NY, United States")
                .suburb("Brooklyn")
                .governmentArea("Williamsburg")
                .market("New York")
                .country("United States")
                .countryCode("US")
                .location(location)
                .build();
    }
    
    private Address createSampleAddress3() {
        Address.Location location = Address.Location.builder()
                .type("Point")
                .coordinates(new Double[]{-73.7949, 40.7282})
                .isLocationExact(false)
                .build();
                
        return Address.builder()
                .street("Queens, NY, United States")
                .suburb("Queens")
                .governmentArea("Astoria")
                .market("New York")
                .country("United States")
                .countryCode("US")
                .location(location)
                .build();
    }
    
    private Availability createSampleAvailability() {
        return Availability.builder()
                .availability30(15)
                .availability60(30)
                .availability90(45)
                .availability365(200)
                .build();
    }
    
    private ReviewScores createSampleReviewScores() {
        return ReviewScores.builder()
                .reviewScoresAccuracy(9)
                .reviewScoresCleanliness(10)
                .reviewScoresCheckin(9)
                .reviewScoresCommunication(10)
                .reviewScoresLocation(9)
                .reviewScoresValue(9)
                .reviewScoresRating(92)
                .build();
    }
    
    private Review createSampleReview1() {
        return Review.builder()
                .id("review001")
                .date(LocalDateTime.now().minusDays(30))
                .listingId("sample001")
                .reviewerId("reviewer001")
                .reviewerName("Alice Johnson")
                .comments("Great place to stay! Very clean and comfortable.")
                .build();
    }
    
    private Review createSampleReview2() {
        return Review.builder()
                .id("review002")
                .date(LocalDateTime.now().minusDays(15))
                .listingId("sample002")
                .reviewerId("reviewer002")
                .reviewerName("Bob Wilson")
                .comments("Loved the industrial design and the neighborhood is fantastic!")
                .build();
    }
    
    private Review createSampleReview3() {
        return Review.builder()
                .id("review003")
                .date(LocalDateTime.now().minusDays(45))
                .listingId("sample003")
                .reviewerId("reviewer003")
                .reviewerName("Carol Davis")
                .comments("Perfect for our family vacation. Kids loved having their own rooms!")
                .build();
    }
}