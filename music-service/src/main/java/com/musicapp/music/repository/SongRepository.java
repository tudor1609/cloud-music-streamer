package com.musicapp.music.repository;

import com.musicapp.music.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByGoogleDriveId(String googleDriveId);

    @Query("SELECT s.googleDriveId FROM Song s")
    Set<String> findAllGoogleDriveIds(); // Asta trage doar textele, e super rapid
}