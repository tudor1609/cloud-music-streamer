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
import java.io.InputStream;
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
        // Opțional: Aici poți face o proiecție să nu trimiți pozele în lista mare,
        // dar momentan lăsăm așa să vedem dacă duce memoria.
        return songRepository.findAll();
    }

    @Async
    @Transactional
    public void scanFolder(String folderId) throws IOException {
        String query = "'" + folderId + "' in parents and mimeType contains 'audio/'";
        String pageToken = null;

        // ACUM E EFICIENT: Luăm doar ID-urile, nu și pozele!
        Set<String> existingIds = songRepository.findAllGoogleDriveIds();

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
    }

    // Metoda de stream pe care o caută frontend-ul (404 fix)
    public InputStream getSongStream(String googleDriveId) throws IOException {
        return googleDrive.files().get(googleDriveId).executeMediaAsInputStream();
    }

    private void processSongMetadata(File file) {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("scan_", ".mp3");
            try (FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {
                googleDrive.files().get(file.getId()).executeMediaAndDownloadTo(fos);
            }

            String title = null;
            String artist = null;
            String album = null;
            byte[] albumArt = null;

            try {
                Mp3File mp3file = new Mp3File(tempFile.toFile());
                if (mp3file.hasId3v2Tag()) {
                    var tag = mp3file.getId3v2Tag();
                    title = tag.getTitle();
                    artist = tag.getArtist();
                    album = tag.getAlbum();
                    albumArt = tag.getAlbumImage();
                } else if (mp3file.hasId3v1Tag()) {
                    var tag = mp3file.getId3v1Tag();
                    title = tag.getTitle();
                    artist = tag.getArtist();
                    album = tag.getAlbum();
                }
            } catch (Exception e) {
                log.warn("Nu am putut citi tag-urile ID3 pentru {}, salvez doar datele de baza.", file.getName());
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
            log.info("Adaugat in DB: {}", file.getName());

        } catch (Exception e) {
            log.error("Eroare critica la procesarea fisierului {}: {}", file.getName(), e.getMessage());
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
            }
        }
    }
}