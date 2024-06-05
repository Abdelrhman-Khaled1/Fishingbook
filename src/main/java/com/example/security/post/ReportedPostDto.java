package com.example.security.post;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportedPostDto {
    private Long id;
    private String content;
    private String imageUrl;

    private Long publisherId;
    private String publisherName;
    private String publisherImage;

    private String createdAt;
    private int numberOfLikes;
    private int numberOfComments;

    private int numberOfReports;
}
