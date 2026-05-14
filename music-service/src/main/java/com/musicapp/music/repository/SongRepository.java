package com.musicapp.music.repository;

import com.musicapp.music.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s.googleDriveId FROM Song s")
    Set<String> findAllGoogleDriveIds();

    // Această metodă creează obiecte Song "ușoare", fără poza de album
    @Query("SELECT new com.musicapp.music.model.Song(s.id, s.name, s.title, s.artist, s.album, s.googleDriveId, s.mimeType, null) FROM Song s")
    List<Song> findAllSongsLight();

    // Căutare după artist sau titlu (ignoring case)
    @Query("SELECT new com.musicapp.music.model.Song(s.id, s.name, s.title, s.artist, s.album, s.googleDriveId, s.mimeType, null) FROM Song s " +
            "WHERE lower(s.title) LIKE lower(concat('%', :query, '%')) " +
            "OR lower(s.artist) LIKE lower(concat('%', :query, '%'))")
    List<Song> searchSongs(@Param("query") String query);
}