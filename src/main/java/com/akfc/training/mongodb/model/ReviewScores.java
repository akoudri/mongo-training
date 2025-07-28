package com.akfc.training.mongodb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewScores {
    
    @Field("review_scores_accuracy")
    private Integer reviewScoresAccuracy;
    
    @Field("review_scores_cleanliness")
    private Integer reviewScoresCleanliness;
    
    @Field("review_scores_checkin")
    private Integer reviewScoresCheckin;
    
    @Field("review_scores_communication")
    private Integer reviewScoresCommunication;
    
    @Field("review_scores_location")
    private Integer reviewScoresLocation;
    
    @Field("review_scores_value")
    private Integer reviewScoresValue;
    
    @Field("review_scores_rating")
    private Integer reviewScoresRating;
}