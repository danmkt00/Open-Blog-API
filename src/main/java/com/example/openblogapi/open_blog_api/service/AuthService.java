package com.example.openblogapi.open_blog_api.service;

import com.example.openblogapi.open_blog_api.dto.LoginRequestDTO;
import com.example.openblogapi.open_blog_api.dto.LoginResponseDTO;
import com.example.openblogapi.open_blog_api.dto.RegisterRequestDTO;
import com.example.openblogapi.open_blog_api.exception.UnauthorizedActionException;
import com.example.openblogapi.open_blog_api.exception.UserAlreadyExistsException;
import com.example.openblogapi.open_blog_api.mapper.UserMapper;
import com.example.openblogapi.open_blog_api.model.Role;
import com.example.openblogapi.open_blog_api.model.User;
import com.example.openblogapi.open_blog_api.repository.UserRepository;
import com.example.openblogapi.open_blog_api.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(RegisterRequestDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new UnauthorizedActionException("Email already in use");
        }

        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        userRepo.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedActionException("Login or password is incorrect"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedActionException("Login or password is incorrect");
        }

        String token = jwtUtils.generateToken(user.getEmail(),
                java.util.Map.of("role", user.getRole().name()));

        return userMapper.toDTO(user, token);
    }
}
