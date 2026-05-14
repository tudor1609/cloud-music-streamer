package com.musicapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserActivityDTO {
    private String username;
    private String currentSongTitle;
    private String currentSongArtist;
    private LocalDateTime lastSeen;
}