package com.example.security.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDtoRequest {
    @NotEmpty
    private String content;
    private String imageUrl;
}
