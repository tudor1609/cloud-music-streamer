package com.musicapp.music.repository;

import com.musicapp.music.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByGoogleDriveId(String googleDriveId);
}