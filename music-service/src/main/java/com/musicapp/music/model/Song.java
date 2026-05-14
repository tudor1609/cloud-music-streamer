package com.musicapp.music.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "song", indexes = @Index(columnList = "googleDriveId")) // Adaugă indexul aici
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true) // Asigură-te că e unic pentru viteză
    private String googleDriveId;
    private String mimeType;
}