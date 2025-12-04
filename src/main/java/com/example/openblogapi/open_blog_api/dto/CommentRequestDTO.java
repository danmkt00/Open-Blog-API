package com.example.openblogapi.open_blog_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDTO {

    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 300, message = "Content must be between 1 and 300 characters")
    private String content;

}
