package com.akfc.training.mongodb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "listingsAndReviews")
public class ListingAndReview {
    
    @Id
    private String id;
    
    @Field("listing_url")
    private String listingUrl;
    
    private String name;
    private String summary;
    private String space;
    private String description;
    
    @Field("neighborhood_overview")
    private String neighborhoodOverview;
    
    private String notes;
    private String transit;
    private String access;
    private String interaction;
    
    @Field("house_rules")
    private String houseRules;
    
    @Field("property_type")
    private String propertyType;
    
    @Field("room_type")
    private String roomType;
    
    @Field("bed_type")
    private String bedType;
    
    @Field("minimum_nights")
    private String minimumNights;
    
    @Field("maximum_nights")
    private String maximumNights;
    
    @Field("cancellation_policy")
    private String cancellationPolicy;
    
    @Field("last_scraped")
    private LocalDateTime lastScraped;
    
    @Field("calendar_last_scraped")
    private LocalDateTime calendarLastScraped;
    
    @Field("first_review")
    private LocalDateTime firstReview;
    
    @Field("last_review")
    private LocalDateTime lastReview;
    
    private Integer accommodates;
    private Integer bedrooms;
    private Integer beds;
    
    @Field("number_of_reviews")
    private Integer numberOfReviews;
    
    private BigDecimal bathrooms;
    private List<String> amenities;
    private BigDecimal price;
    
    @Field("security_deposit")
    private BigDecimal securityDeposit;
    
    @Field("cleaning_fee")
    private BigDecimal cleaningFee;
    
    @Field("extra_people")
    private BigDecimal extraPeople;
    
    @Field("guests_included")
    private BigDecimal guestsIncluded;
    
    private Images images;
    private Host host;
    private Address address;
    private Availability availability;
    
    @Field("review_scores")
    private ReviewScores reviewScores;
    
    private List<Review> reviews;
}