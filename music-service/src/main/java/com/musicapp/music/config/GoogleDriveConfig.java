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
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {

    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        // Punem tot continutul fisierului JSON intr-un Text Block
        // Asigura-te ca este EXACT continutul fisierului descarcat de la Google
        String googleAuthJson = """
                {
                  "type": "service_account",
                  "project_id": "mycloudmusic-496116",
                  "private_key_id": "22c582d1b89cc39b08320ed0bf6e73cd94748658",
                  "private_key": "-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDHnhmKJymFnJsK\\nWlebwmEn46UkikJfiEn8nJv/OJ3luSKOGeCdNm7yeEGGtCv/poZpkArzUXKuessi\\nk6zo1J5oGrMlfPenTZl5IDIK9wzBYdPhf/W03s1THmDUOIgQv32h41UNg2huf0+/\\nUrS3egC9snI2jnAkn66r8ljaYgas5sBfiFy/ZNWVefgbevj3KrgcDmtwEEkPrAus\\nWhIf0qS8RgcZ5XjU75odiamKIiDVgQA0qVJJvdXB7qLIVAI/09fnPyQPM7Sj89Ap\\n02hFTgBwWzKrh9BunZiU1+DM5OLaetShHFk7NJMqCDFgHpuMzUugK4rhjTfjDwZL\\nZBdmQfXPAgMBAAECggEADgqe/UrX7uAfP9EvDHPV5bprqXBYTuQFYFGb344Z/UjS\\nl3BWjnh/wWqYIWOXHLqYBeDuBEOcb42PsHT22sHqCqSmKWLrAMfCnfSX0JGkQg4h\\nm2Em5XwusUQXx5XabGMr8TFOkE7ID83AzFKDB8z7NFzTD+dhGBJrdQjtGx9+HLdW\\nwlkieLtQJZ+PpWeuOIlpkZLHMdCy1Dxd8fAyFyskX39ipexswdmTBTnCYywLbmGY\\nwkyU1ur7wIsJk6bQ+Sw2IvMQFy71VI8V5mGkXYpyUPyNAFOr6L5Evr6AAuj4t7IT\\ntHuliGOnJ1bFPp0LCGcabp4zUWO/sHzVRIXoeCacYQKBgQDvKHfQqgJbWcjdwbnD\\njWIR7tg1klFNhbDZ8Y6PzEqpYrihZB5wEdwHgAKjtMH6RcbcA2PztG4rKMtvOpM4\\nmrEM9y8yo41eKEmC4qwnybBEwKs9f8lRSyFUHLlJxzihSYb2OHVQt1kvKQt7iBRK\\n+Sh0Ch3T58p7ogf3i98pq81BSQKBgQDVrMwWQNJ9vDUygjgRFWV2zx9J74Wjagot\\nfgklvDHu/H66mbrj/CT2FVItp0KvenT/NtxaDrZ5M5UIwZj+ET7Rpeqt4W3jq7t9\\nuyDhPHzBYci5tBeOVMmYfNN4Le8i/KO280uEPLX84eTMpWEvgsE6VihDKOc3E5Lm\\nPvRFBUCWVwKBgQDK812R/uPXH788qIx67Xz+2OAzvgIutjgZC0s+rk/hpHwnwW2v\\nDwjYeIJE6D0ApLwXsuzcQA6Jo6nXry6GB4geB5nO8EokUC3p22/ap+ZkwdfPbaSq\\nzwXueh2nBWACRwI36jtsDtupELzEzPrjkMMKjhc332jON/do1BLCKyAYuQKBgQCb\\nqPFYeMqe/IAIlqK/l0W+Yq8Zjptda7+UWo/RZKM/xcOQrrNeqO5IM0oVphbHfT9E\\nOTQXi0ZVT0WQBnEtDWUlF2i6WVJTnGTO+IbAbu83dcPjvRW4A36FTtJDZ7shUsj1\\nW5I0+IFfgzTJ/OSq1udrVx6npmGiVukaQNx0d5oQqwKBgQDEskhvTxJXIqWLO7Nl\\nWC9I2drGJ9XK7vzz5y7nvE+4mwUgWZ5IGyrjGWy0RbfJ8jp1m8h7CKrrTniYizpD\\na2zHaqb/Lc5K5UxdOMZm7XYJHlw+hQ0XDk0abv/i2W7xOGGqAk3a1iF94kgYV9LT\\nA3YEPCfxHCe61AhU+oGr77uV1A==\\n-----END PRIVATE KEY-----\\n",
                  "client_email": "music-streamer-account@mycloudmusic-496116.iam.gserviceaccount.com",
                  "client_id": "102494740727053492848",
                  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
                  "token_uri": "https://oauth2.googleapis.com/token",
                  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
                  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/music-streamer-account%40mycloudmusic-496116.iam.gserviceaccount.com",
                  "universe_domain": "googleapis.com"
                }
                """;

        // Convertim String-ul inapoi in Stream pentru a fi consumat de biblioteca Google
        ByteArrayInputStream stream = new ByteArrayInputStream(googleAuthJson.getBytes(StandardCharsets.UTF_8));

        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(stream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE_READONLY));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("CloudMusicStreamer")
                .build();
    }
}
