package com.akfc.training.mongodb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Host {
    
    @Field("host_id")
    private String hostId;
    
    @Field("host_url")
    private String hostUrl;
    
    @Field("host_name")
    private String hostName;
    
    @Field("host_location")
    private String hostLocation;
    
    @Field("host_about")
    private String hostAbout;
    
    @Field("host_response_time")
    private String hostResponseTime;
    
    @Field("host_thumbnail_url")
    private String hostThumbnailUrl;
    
    @Field("host_picture_url")
    private String hostPictureUrl;
    
    @Field("host_neighbourhood")
    private String hostNeighbourhood;
    
    @Field("host_response_rate")
    private Integer hostResponseRate;
    
    @Field("host_is_superhost")
    private Boolean hostIsSuperhost;
    
    @Field("host_has_profile_pic")
    private Boolean hostHasProfilePic;
    
    @Field("host_identity_verified")
    private Boolean hostIdentityVerified;
    
    @Field("host_listings_count")
    private Integer hostListingsCount;
    
    @Field("host_total_listings_count")
    private Integer hostTotalListingsCount;
    
    @Field("host_verifications")
    private List<String> hostVerifications;
}