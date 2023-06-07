package com.damda.back.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Properties {
    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("thumbnail_image")
    private String thumbnailImage;

}
