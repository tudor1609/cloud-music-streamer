package com.musicapp.music.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        // Hardcodăm direct bucățile esențiale din JSON-ul tău
        String clientEmail = "music-streamer-account@mycloudmusic-496116.iam.gserviceaccount.com";
        
        // Folosim Text Block (Java 15+) pentru a păstra formatul RSA intact
        String privateKey = """
                -----BEGIN PRIVATE KEY-----
                MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDHnhmKJymFnJsK
                WlebwmEn46UkikJfiEn8nJv/OJ3luSKOGeCdNm7yeEGGtCv/poZpkArzUXKuessi
                k6zo1J5oGrMlfPenTZl5IDIK9wzBYdPhf/W03s1THmDUOIgQv32h41UNg2huf0+/
                UrS3egC9snI2jnAkn66r8ljaYgas5sBfiFy/ZNWVefgbevj3KrgcDmtwEEkPrAus
                WhIf0qS8RgcZ5XjU75odiamKIiDVgQA0qVJJvdXB7qLIVAI/09fnPyQPM7Sj89Ap
                02hFTgBwWzKrh9BunZiU1+DM5OLaetShHFk7NJMqCDFgHpuMzUugK4rhjTfjDwZL
                ZBdmQfXPAgMBAAECggEADgqe/UrX7uAfP9EvDHPV5bprqXBYTuQFYFGb344Z/UjS
                l3BWjnh/wWqYIWOXHLqYBeDuBEOcb42PsHT22sHqCqSmKWLrAMfCnfSX0JGkQg4h
                m2Em5XwusUQXx5XabGMr8TFOkE7ID83AzFKDB8z7NFzTD+dhGBJrdQjtGx9+HLdW
                wlkieLtQJZ+PpWeuOIlpkZLHMdCy1Dxd8fAyFyskX39ipexswdmTBTnCYywLbmGY
                wkyU1ur7wIsJk6bQ+Sw2IvMQFy71VI8V5mGkXYpyUPyNAFOr6L5Evr6AAuj4t7IT
                tHuliGOnJ1bFPp0LCGcabp4zUWO/sHzVRIXoeCacYQKBgQDvKHfQqgJbWcjdwbnD
                jWIR7tg1klFNhbDZ8Y6PzEqpYrihZB5wEdwHgAKjtMH6RcbcA2PztG4rKMtvOpM4
                mrEM9y8yo41eKEmC4qwnybBEwKs9f8lRSyFUHLlJxzihSYb2OHVQt1kvKQt7iBRK
                +Sh0Ch3T58p7ogf3i98pq81BSQKBgQDVrMwWQNJ9vDUygjgRFWV2zx9J74Wjagot
                fgklvDHu/H66mbrj/CT2FVItp0KvenT/NtxaDrZ5M5UIwZj+ET7Rpeqt4W3jq7t9
                uyDhPHzBYci5tBeOVMmYfNN4Le8i/KO280uEPLX84eTMpWEvgsE6VihDKOc3E5Lm
                PvRFBUCWVwKBgQDK812R/uPXH788qIx67Xz+2OAzvgIutjgZC0s+rk/hpHwnwW2v
                DwjYeIJE6D0ApLwXsuzcQA6Jo6nXry6GB4geB5nO8EokUC3p22/ap+ZkwdfPbaSq
                zwXueh2nBWACRwI36jtsDtupELzEzPrjkMMKjhc332jON/do1BLCKyAYuQKBgQCb
                qPFYeMqe/IAIlqK/l0W+Yq8Zjptda7+UWo/RZKM/xcOQrrNeqO5IM0oVphbHfT9E
                OTQXi0ZVT0WQBnEtDWUlF2i6WVJTnGTO+IbAbu83dcPjvRW4A36FTtJDZ7shUsj1
                W5I0+IFfgzTJ/OSq1udrVx6npmGiVukaQNx0d5oQqwKBgQDEskhvTxJXIqWLO7Nl
                WC9I2drGJ9XK7vzz5y7nvE+4mwUgWZ5IGyrjGWy0RbfJ8jp1m8h7CKrrTniYizpD
                a2zHaqb/Lc5K5UxdOMZm7XYJHlw+hQ0XDk0abv/i2W7xOGGqAk3a1iF94kgYV9LT
                A3YEPCfxHCe61AhU+oGr77uV1A==
                -----END PRIVATE KEY-----
                """;

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
