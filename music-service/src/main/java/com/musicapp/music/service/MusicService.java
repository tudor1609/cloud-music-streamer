package com.musicapp.music.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.mpatric.mp3agic.Mp3File;
import com.musicapp.music.model.Song;
import com.musicapp.music.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicService {

    private final Drive googleDrive;
    private final SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Async
    @Transactional
    public void scanFolder(String folderId) throws IOException {
        String query = "'" + folderId + "' in parents and mimeType contains 'audio/'";
        String pageToken = null;

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
                    if (!existingIds.contains(file.getId())) {
                        processSongMetadata(file);
                        existingIds.add(file.getId());
                    }
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        log.info("Scanare completa pentru folderul: {}", folderId);
    }

    private void processSongMetadata(File file) {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("scan_", ".mp3");
            try (FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {
                googleDrive.files().get(file.getId()).executeMediaAndDownloadTo(fos);
            }

            Mp3File mp3file = new Mp3File(tempFile.toFile());
            String title = file.getName();
            String artist = "Unknown Artist";
            String album = "Unknown Album";
            byte[] albumArt = null;

            if (mp3file.hasId3v2Tag()) {
                var tag = mp3file.getId3v2Tag();
                title = tag.getTitle() != null ? tag.getTitle() : title;
                artist = tag.getArtist() != null ? tag.getArtist() : artist;
                album = tag.getAlbum() != null ? tag.getAlbum() : album;
                albumArt = tag.getAlbumImage();
            }

            Song song = Song.builder()
                    .name(file.getName())
                    .title(title)
                    .artist(artist)
                    .album(album)
                    .albumArt(albumArt)
                    .googleDriveId(file.getId())
                    .mimeType(file.getMimeType())
                    .build();

            songRepository.save(song);
            log.info("Adaugat: {} - {}", artist, title);

        } catch (Exception e) {
            log.error("Eroare la {} : {}", file.getName(), e.getMessage());
            songRepository.save(Song.builder()
                    .name(file.getName())
                    .title(file.getName())
                    .googleDriveId(file.getId())
                    .mimeType(file.getMimeType())
                    .build());
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
            }
        }
    }
}