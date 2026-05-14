package com.musicapp.music.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.musicapp.music.model.Song;
import com.musicapp.music.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final Drive googleDrive;
    private final SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    /**
     * Scanează folderul asincron și folosește paginarea pentru a găsi TOATE piesele.
     * @Async face ca această metodă să ruleze pe un fir de execuție separat.
     */
    @Async
    @Transactional
    public void scanFolder(String folderId) throws IOException {
        String query = "'" + folderId + "' in parents and mimeType contains 'audio/'";
        String pageToken = null;

        // Optimizare: Luăm toate ID-urile existente o singură dată pentru a evita SELECT-uri repetate în buclă
        Set<String> existingIds = songRepository.findAll().stream()
                .map(Song::getGoogleDriveId)
                .collect(Collectors.toSet());

        do {
            FileList result = googleDrive.files().list()
                    .setQ(query)
                    .setFields("nextPageToken, files(id, name, mimeType)")
                    .setPageToken(pageToken)
                    .execute();

            List<File> files = result.getFiles();
            if (files != null) {
                for (File file : files) {
                    // Verificăm în memorie (Set) dacă piesa există deja - mult mai rapid
                    if (!existingIds.contains(file.getId())) {
                        Song song = Song.builder()
                                .name(file.getName())
                                .googleDriveId(file.getId())
                                .mimeType(file.getMimeType())
                                .build();
                        songRepository.save(song);
                        // Adăugăm în set-ul local pentru a preveni duplicatele în cadrul aceleiași scanări
                        existingIds.add(file.getId());
                    }
                }
            }

            pageToken = result.getNextPageToken();
        } while (pageToken != null);
    }

    public InputStream streamSong(String googleDriveId) throws IOException {
        return googleDrive.files().get(googleDriveId).executeMediaAsInputStream();
    }
}