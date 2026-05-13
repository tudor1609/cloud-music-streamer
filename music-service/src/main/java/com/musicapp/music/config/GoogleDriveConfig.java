package com.musicapp.music.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream; // IMPORTUL LIPSĂ!
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        // Citim fisierul exact asa cum este el, fara sa mai atingem string-ul cheii
        try (InputStream in = getClass().getResourceAsStream("/google-auth.json")) {
            if (in == null) {
                throw new IOException("Fisierul google-auth.json nu a fost gasit in resurse!");
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE_READONLY));

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("CloudMusicStreamer")
                    .build();
        }
    }
}
