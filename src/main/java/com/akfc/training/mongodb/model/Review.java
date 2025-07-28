package com.akfc.training.mongodb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    
    @Field("_id")
    private String id;
    
    private LocalDateTime date;
    
    @Field("listing_id")
    private String listingId;
    
    @Field("reviewer_id")
    private String reviewerId;
    
    @Field("reviewer_name")
    private String reviewerName;
    
    private String comments;
}