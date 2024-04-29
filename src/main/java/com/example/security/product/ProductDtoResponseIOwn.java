package com.example.security.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDtoResponseIOwn {
    private Long id;
    private String title;
    private String content;
    private int price;
    private String imageUrl;
    private Long categoryId;
    private String createdOn;
    private String updatedOn;
}
