package com.musicapp.music.controller;

import com.musicapp.music.model.Song;
import com.musicapp.music.repository.SongRepository;
import com.musicapp.music.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;
    private final SongRepository songRepository;

    @GetMapping("/songs")
    public List<Song> getSongs() {
        return musicService.getAllSongs();
    }

    @GetMapping("/scan")
    public String scan(@RequestParam String folderId) {
        try {
            musicService.scanFolder(folderId);
            return "Scanare pornita asincron...";
        } catch (Exception e) {
            return "Eroare: " + e.getMessage();
        }
    }

    @GetMapping("/stream/{googleDriveId}")
    public ResponseEntity<InputStreamResource> streamSong(@PathVariable String googleDriveId) {
        try {
            var stream = musicService.getSongStream(googleDriveId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(new InputStreamResource(stream));
        } catch (IOException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/songs/{id}/album-art")
    public ResponseEntity<byte[]> getAlbumArt(@PathVariable Long id) {
        return songRepository.findById(id)
                .map(song -> {
                    if (song.getAlbumArt() == null) return ResponseEntity.notFound().<byte[]>build();
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(song.getAlbumArt());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}