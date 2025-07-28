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
public class Images {
    
    @Field("thumbnail_url")
    private String thumbnailUrl;
    
    @Field("medium_url")
    private String mediumUrl;
    
    @Field("picture_url")
    private String pictureUrl;
    
    @Field("xl_picture_url")
    private String xlPictureUrl;
}