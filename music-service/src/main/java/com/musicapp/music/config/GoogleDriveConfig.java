package com.musicapp.music.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    private static final Logger log = LoggerFactory.getLogger(GoogleDriveConfig.class);

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        log.info("--- [DEBUG] Începerea procesului de inițializare Google Drive Client ---");

        // Încercăm să localizăm fișierul în resurse
        String resourcePath = "/google-auth.json";
        
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                log.error("--- [DEBUG] EROARE: Fișierul {} NU a fost găsit în src/main/resources/ ---", resourcePath);
                throw new IOException("Fisierul google-auth.json nu a fost gasit in resurse!");
            }

            log.info("--- [DEBUG] Fișierul {} a fost găsit. Încercăm parsarea credențialelor... ---", resourcePath);

            GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE_READONLY));

            log.info("--- [DEBUG] Credențiale încărcate cu succes! Tip: {} ---", credentials.getClass().getName());

            Drive drive = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("CloudMusicStreamer")
                    .build();

            log.info("--- [DEBUG] Google Drive Client a fost creat cu succes. Gata pentru scanare! ---");
            return drive;

        } catch (Exception e) {
            log.error("--- [DEBUG] EROARE CRITICĂ la inițializarea Google Drive: {} ---", e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
