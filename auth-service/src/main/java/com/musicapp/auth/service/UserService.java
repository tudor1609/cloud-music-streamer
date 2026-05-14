package com.musicapp.auth.service;

import com.musicapp.auth.model.User;
import com.musicapp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> searchUsers(String query) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Transactional // Asigură că ambele salvări se fac împreună
    public void addFriend(String currentUsername, String friendUsername) {
        if (currentUsername.equalsIgnoreCase(friendUsername)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nu te poți adăuga singur!");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Userul tău nu există"));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prietenul nu a fost găsit"));

        // Adăugăm în ambele părți pentru reciprocitate
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userRepository.save(user);
        userRepository.save(friend);
    }

    public Set<User> getFriends(String username) {
        return getUserByUsername(username).getFriends();
    }
    @Transactional
    public void updateActivity(String username, String title, String artist) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setCurrentSongTitle(title);
        user.setCurrentSongArtist(artist);
        user.setLastSeen(java.time.LocalDateTime.now());
        userRepository.save(user);
    }
}