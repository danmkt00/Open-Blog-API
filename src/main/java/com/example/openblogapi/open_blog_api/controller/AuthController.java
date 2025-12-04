package com.example.openblogapi.open_blog_api.controller;

import com.example.openblogapi.open_blog_api.dto.LoginRequestDTO;
import com.example.openblogapi.open_blog_api.dto.LoginResponseDTO;
import com.example.openblogapi.open_blog_api.dto.RegisterRequestDTO;
import com.example.openblogapi.open_blog_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO dto) {
        auth.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Login and get JWT
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = auth.login(dto);
        return ResponseEntity.ok(response);
    }
}
