package com.musicapp.music.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.musicapp.music.model.Song;
import com.musicapp.music.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final Drive googleDrive;
    private final SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    /**
     * Scanează folderul și folosește paginarea pentru a găsi TOATE piesele.
     */
    public void scanFolder(String folderId) throws IOException {
        String query = "'" + folderId + "' in parents and mimeType contains 'audio/'";
        String pageToken = null;

        do {
            // Cerem lista de fișiere, incluzând nextPageToken pentru paginare
            FileList result = googleDrive.files().list()
                    .setQ(query)
                    .setFields("nextPageToken, files(id, name, mimeType)")
                    .setPageToken(pageToken)
                    .execute();

            List<File> files = result.getFiles();
            if (files != null) {
                for (File file : files) {
                    // Verificăm dacă piesa există deja după ID-ul de Drive ca să nu o dublăm
                    if (songRepository.findByGoogleDriveId(file.getId()).isEmpty()) {
                        Song song = Song.builder()
                                .name(file.getName())
                                .googleDriveId(file.getId())
                                .mimeType(file.getMimeType())
                                .build();
                        songRepository.save(song);
                    }
                }
            }

            // Preluăm token-ul pentru pagina următoare (va fi null când terminăm)
            pageToken = result.getNextPageToken();

        } while (pageToken != null);
    }

    public InputStream streamSong(String googleDriveId) throws IOException {
        return googleDrive.files().get(googleDriveId).executeMediaAsInputStream();
    }
}