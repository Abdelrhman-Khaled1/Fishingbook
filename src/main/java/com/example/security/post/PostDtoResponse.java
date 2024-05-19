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
    private int numberOfComments;

    private boolean isPostLiked;

    public PostDtoResponse(Long id, String content, String imageUrl, Long publisherId, String publisherName, String publisherImageUrl, String createdAt, int numberOfLikes, int numberOfComments) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.publisherImageUrl = publisherImageUrl;
        this.createdAt = createdAt;
        this.numberOfLikes = numberOfLikes;
        this.numberOfComments = numberOfComments;
    }
}
