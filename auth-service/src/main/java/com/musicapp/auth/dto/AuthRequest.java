package com.musicapp.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email; // Adăugat pentru înregistrare
}