package com.musicapp.music.controller;

import com.musicapp.music.model.Song;
import com.musicapp.music.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
// NU punem @RequestMapping aici deoarece Gateway-ul trimite rutele deja "curatate" (ex: /songs, /scan)
public class MusicController {

    private final MusicService musicService;

    /**
     * Returnează lista tuturor melodiilor din baza de date.
     * Accesibil via Gateway la: GET http://localhost:8080/api/music/songs
     */
    @GetMapping("/songs")
    public List<Song> getSongs() {
        return musicService.getAllSongs();
    }

    /**
     * Scanează un folder din Google Drive pentru a adăuga melodii noi.
     * Accesibil via Gateway la: GET http://localhost:8080/api/music/scan?folderId=XXX
     */
    @GetMapping("/scan")
    public String scan(@RequestParam String folderId) {
        try {
            musicService.scanFolder(folderId);
            return "Scanare terminata cu succes pentru folderul: " + folderId;
        } catch (Exception e) {
            return "Eroare la scanare: " + e.getMessage();
        }
    }

    /**
     * Stream audio pentru o melodie specifică folosind ID-ul de Google Drive.
     * Accesibil via Gateway la: GET http://localhost:8080/api/music/stream/{driveId}
     */
    @GetMapping("/stream/{driveId}")
    public ResponseEntity<InputStreamResource> stream(@PathVariable String driveId) {
        try {
            InputStream stream = musicService.streamSong(driveId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint de test pentru a verifica daca serviciul raspunde.
     * Accesibil via Gateway la: GET http://localhost:8080/api/music/test
     */
    @GetMapping("/test")
    public String test() {
        return "Salut! Music Service e sus si gata de treaba!";
    }
}