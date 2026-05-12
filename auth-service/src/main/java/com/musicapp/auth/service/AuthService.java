package com.musicapp.auth.service;

import com.musicapp.auth.dto.AuthRequest;
import com.musicapp.auth.model.User;
import com.musicapp.auth.repository.UserRepository;
import com.musicapp.auth.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public String register(AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getUsername());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}