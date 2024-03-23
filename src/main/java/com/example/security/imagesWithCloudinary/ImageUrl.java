package com.example.security.imagesWithCloudinary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ImageUrl {
    @JsonProperty("url")
    String url;
}
