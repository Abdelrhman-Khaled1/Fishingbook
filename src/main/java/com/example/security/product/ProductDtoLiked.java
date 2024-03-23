package com.example.security.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDtoLiked {

    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private int price;
    private String imageUrl;
    private boolean liked;
}
