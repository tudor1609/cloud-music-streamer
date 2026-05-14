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
    private final EmailService emailService; // Injectăm serviciul de email

    public String register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Utilizatorul există deja!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail()) // Salvăm email-ul
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        // Trimitem mail-ul de bun venit în fundal
        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());

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