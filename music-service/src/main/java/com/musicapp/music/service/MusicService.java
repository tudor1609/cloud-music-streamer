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
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final Drive googleDrive;
    private final SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public void scanFolder(String folderId) throws IOException {
        String query = "'" + folderId + "' in parents and mimeType contains 'audio/'";

        FileList result = googleDrive.files().list()
                .setQ(query)
                .setFields("files(id, name, mimeType)")
                .execute();

        List<File> files = result.getFiles();
        for (File file : files) {
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

    public InputStream streamSong(String googleDriveId) throws IOException {
        return googleDrive.files().get(googleDriveId).executeMediaAsInputStream();
    }
}