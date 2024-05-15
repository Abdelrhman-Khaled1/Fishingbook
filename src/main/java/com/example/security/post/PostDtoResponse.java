package com.example.security.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostDtoResponse {
    private Long id;
    private String content;
    private String imageUrl;

    private Long publisherId;
    private String publisherName;
    private String publisherImageUrl;

    private String createdAt;
    private int numberOfLikes;
}
