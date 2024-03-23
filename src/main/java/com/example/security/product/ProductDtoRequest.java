package com.example.security.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDtoRequest {

    private String content;
    private String title;
    private Long categoryId;
    private int price;
    private String imageUrl;
}
