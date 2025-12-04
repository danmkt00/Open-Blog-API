package com.example.openblogapi.open_blog_api.mapper;

import com.example.openblogapi.open_blog_api.dto.LoginResponseDTO;
import com.example.openblogapi.open_blog_api.dto.RegisterRequestDTO;
import com.example.openblogapi.open_blog_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPasswordHash(registerRequestDTO.getPassword());
        return user;
    }

    public LoginResponseDTO toDTO(User user, String token) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setToken(token);
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}

