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

    private String name;
    private String title;
    private String artist;
    private String album;
    private String googleDriveId;
    private String mimeType;

    @Lob
    @Column(name = "album_art", columnDefinition = "bytea")
    private byte[] albumArt;
}