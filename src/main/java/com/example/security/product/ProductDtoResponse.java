package com.example.security.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDtoResponse {

    private Long id;
    private String content;
    private String title;
    private Long publisherId;
}
