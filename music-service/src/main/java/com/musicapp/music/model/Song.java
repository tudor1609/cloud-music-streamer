package com.musicapp.music.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "song", indexes = @Index(columnList = "googleDriveId"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Numele brut al fișierului (ex: piesa1.mp3)

    private String title;  // Poate fi null
    private String artist; // Poate fi null
    private String album;  // Poate fi null

    private String googleDriveId;
    private String mimeType;

    @Column(name = "album_art", columnDefinition = "bytea") // Fără @Lob pentru Postgres
    private byte[] albumArt; // Poate fi null
}