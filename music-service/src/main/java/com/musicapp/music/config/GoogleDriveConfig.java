package com.musicapp.music.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream; // Importul nou
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        String base64Key = System.getenv("KEY_BASE64");
        
        if (base64Key == null || base64Key.isEmpty()) {
            // Fallback pentru local - asigura-te ca ai google-auth.json in src/main/resources
            InputStream in = getClass().getResourceAsStream("/google-auth.json");
            if (in == null) {
                throw new IOException("Nu s-a gasit KEY_BASE64 in mediu si nici google-auth.json in resurse!");
            }
            return createDrive(in);
        }

        // Decodificare din variabila de mediu (pentru Railway)
        byte[] decodedJson = java.util.Base64.getDecoder().decode(base64Key.trim());
        return createDrive(new ByteArrayInputStream(decodedJson));
    }

    private Drive createDrive(InputStream in) throws IOException, GeneralSecurityException {
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
