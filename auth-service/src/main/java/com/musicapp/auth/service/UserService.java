package com.musicapp.auth.service;

import com.musicapp.auth.model.User;
import com.musicapp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void addFriend(String currentUsername, String friendUsername) {
        User user = getUserByUsername(currentUsername);
        User friend = getUserByUsername(friendUsername);

        if (user.getUsername().equals(friend.getUsername())) {
            throw new RuntimeException("Nu te poți adăuga pe tine însuți!");
        }

        user.getFriends().add(friend);
        userRepository.save(user);
    }

    public Set<User> getFriends(String username) {
        return getUserByUsername(username).getFriends();
    }
}