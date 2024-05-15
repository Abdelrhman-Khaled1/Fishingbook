package com.example.security.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDtoUpdate {
    private Long id;
    @NotEmpty
    private String content;
    private String imageUrl;
}
