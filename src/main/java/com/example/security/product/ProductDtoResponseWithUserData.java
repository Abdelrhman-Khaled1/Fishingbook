package com.example.security.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDtoResponseWithUserData {
    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private String publisherName;
    private String publisherImage;
    private Instant createdOn;
    private Instant updatedOn;
    private int price;
    private String imageUrl;
}
