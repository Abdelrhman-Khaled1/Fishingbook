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
public class ProductDtoLiked {

    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private Long publisherId;
    private String publisherName;
    private String publisherImage;
    private String createdOn;
    private String updatedOn;
    private int price;
    private String imageUrl;
    private boolean liked;

    public ProductDtoLiked(Long id, String title, String content, Long categoryId, Long publisherId, String publisherName, String publisherImage, String  createdOn, String  updatedOn, int price, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.publisherImage = publisherImage;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
