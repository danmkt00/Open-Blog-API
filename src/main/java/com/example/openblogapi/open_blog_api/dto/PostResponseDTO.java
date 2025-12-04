package com.example.openblogapi.open_blog_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String authorUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //comment
    private long commentCount;
    //like
    private long likeCount;
    private boolean likedByCurrentUser;

}