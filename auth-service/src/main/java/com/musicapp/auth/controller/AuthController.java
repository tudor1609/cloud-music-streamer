package com.musicapp.auth.controller;

import com.musicapp.auth.dto.AuthRequest;
import com.musicapp.auth.dto.AuthResponse;
import com.musicapp.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
// !!! AM ȘTERS @RequestMapping("/auth") DE AICI !!!
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}