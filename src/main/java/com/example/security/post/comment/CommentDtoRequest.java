package com.example.security.post.comment;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDtoRequest {
    private String content;
}
