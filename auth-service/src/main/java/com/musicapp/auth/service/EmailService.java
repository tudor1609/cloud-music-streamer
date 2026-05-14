package com.musicapp.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendWelcomeEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Bun venit pe Dracify, " + username + "!");
        message.setText("Salut!\n\nNe bucurăm că te-ai alăturat platformei noastre de streaming. " +
                "Acum poți să îți asculți muzica preferată direct din cloud.\n\n" +
                "Cu drag,\nEchipa Dracify");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea mail-ului: " + e.getMessage());
        }
    }
}