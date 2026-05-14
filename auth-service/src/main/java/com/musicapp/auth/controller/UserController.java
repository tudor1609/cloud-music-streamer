package com.musicapp.auth.controller;

import com.musicapp.auth.model.User;
import com.musicapp.auth.dto.UserActivityDTO;
import com.musicapp.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public User getMyProfile(@RequestHeader("X-User-Name") String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/search")
    public List<User> search(@RequestParam String q) {
        return userService.searchUsers(q);
    }

    @PostMapping("/friends/add")
    public String addFriend(@RequestHeader("X-User-Name") String currentUsername,
                            @RequestParam String friendUsername) {
        userService.addFriend(currentUsername, friendUsername);
        return "Prieten adăugat!";
    }

    @GetMapping("/friends")
    public Set<User> getFriends(@RequestHeader("X-User-Name") String username) {
        return userService.getFriends(username);
    }

    @PostMapping("/me/activity")
    public void updateMyActivity(@RequestHeader("X-User-Name") String username,
                                 @RequestParam String title,
                                 @RequestParam String artist) {
        userService.updateActivity(username, title, artist);
    }

    @GetMapping("/friends/activity")
    public List<UserActivityDTO> getFriendsActivity(@RequestHeader("X-User-Name") String username) {
        return userService.getFriends(username).stream()
                .map(f -> new UserActivityDTO(
                        f.getUsername(),
                        f.getCurrentSongTitle(),
                        f.getCurrentSongArtist(),
                        f.getLastSeen()))
                .toList();
    }
}