package com.example.security.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDtoUpdate {

    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private int price;
    private String imageUrl;
}
