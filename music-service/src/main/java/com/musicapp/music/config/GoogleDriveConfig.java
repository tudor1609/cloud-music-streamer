package com.musicapp.music.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        // Citim variabilele de mediu din Railway
        String clientEmail = System.getenv("GOOGLE_CLIENT_EMAIL");
        String privateKeyRaw = System.getenv("GOOGLE_PRIVATE_KEY");

        if (clientEmail == null || privateKeyRaw == null) {
            throw new IllegalStateException("Variabilele GOOGLE_CLIENT_EMAIL sau GOOGLE_PRIVATE_KEY lipsesc!");
        }

        // FIX-UL CRITIC: Inlocuim \n simbolic cu newline real
        String privateKey = privateKeyRaw.replace("\\n", "\n");

        // Construim credentialele manual din string-uri, fara a mai depinde de fisierul .json
        ServiceAccountCredentials credentials = ServiceAccountCredentials.newBuilder()
                .setClientEmail(clientEmail)
                .setPrivateKeyString(privateKey)
                .setScopes(Collections.singleton(DriveScopes.DRIVE_READONLY))
                .build();

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("CloudMusicStreamer")
                .build();
    }
}
