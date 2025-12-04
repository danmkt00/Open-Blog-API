package com.example.openblogapi.open_blog_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class LoginResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
