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
public class Availability {
    
    @Field("availability_30")
    private Integer availability30;
    
    @Field("availability_60")
    private Integer availability60;
    
    @Field("availability_90")
    private Integer availability90;
    
    @Field("availability_365")
    private Integer availability365;
}