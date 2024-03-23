package com.example.security.product;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDtoResponse {

    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private int price;
    private String imageUrl;
}
