package com.example.security.post.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentDtoResponse {

    private Long id;
    private String content;

    private Long publisherId;
    private String publisherName;
    private String publisherImageUrl;

    private String createdAt;

}
