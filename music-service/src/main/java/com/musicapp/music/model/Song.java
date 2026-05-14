package com.musicapp.music.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // Importă asta!
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

    private String name;
    private String title;
    private String artist;
    private String album;
    private String googleDriveId;
    private String mimeType;

    @JsonIgnore // Aceasta linie este vitala pentru viteza si CORS
    @Column(name = "album_art", columnDefinition = "bytea")
    private byte[] albumArt;
}