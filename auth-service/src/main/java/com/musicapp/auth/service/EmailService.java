package com.musicapp.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // Aceasta adnotare ne permite să folosim obiectul "log" direct
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendWelcomeEmail(String to, String username) {
        log.info("DEBUG: [START] Incepe procesul de trimitere mail asincron pentru user-ul: {}", username);
        log.info("DEBUG: Se incearca trimiterea catre adresa: {}", to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Dracify <adresa_ta_de_mail@gmail.com>"); // E bine sa pui si From-ul explicit
            message.setTo(to);
            message.setSubject("Bun venit pe Dracify, " + username + "!");
            message.setText("Salut " + username + ",\n\n" +
                    "Ne bucurăm că te-ai alăturat platformei noastre de streaming. " +
                    "Acum poți să îți asculți muzica preferată direct din cloud.\n\n" +
                    "Cu drag,\nEchipa Dracify");

            log.info("DEBUG: Se apeleaza mailSender.send()... (Aici poate dura daca sunt probleme de conexiune)");

            mailSender.send(message);

            log.info("DEBUG: [SUCCESS] Mail-ul a fost trimis cu succes catre {}!", to);

        } catch (Exception e) {
            log.error("DEBUG: [ERROR] A aparut o eroare la trimiterea mail-ului catre: {}", to);
            log.error("DEBUG: Mesaj eroare: {}", e.getMessage());

            // Aceasta linie va printa toata eroarea (stack trace-ul) ca sa vedem daca e Timeout sau Auth
            e.printStackTrace();
        }
    }
}