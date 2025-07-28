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
public class Address {
    
    private String street;
    private String suburb;
    
    @Field("government_area")
    private String governmentArea;
    
    private String market;
    private String country;
    
    @Field("country_code")
    private String countryCode;
    
    private Location location;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private String type;
        private Double[] coordinates;
        
        @Field("is_location_exact")
        private Boolean isLocationExact;
    }
}