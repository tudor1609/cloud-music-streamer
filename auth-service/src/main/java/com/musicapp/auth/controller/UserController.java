package com.musicapp.auth.controller;

import com.musicapp.auth.model.User;
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

    // Endpoint pentru profilul propriu
    @GetMapping("/me")
    public User getMyProfile(@RequestHeader("X-User-Name") String username) {
        return userService.getUserByUsername(username);
    }

    // Căutare utilizatori după nume
    @GetMapping("/search")
    public List<User> search(@RequestParam String q) {
        return userService.searchUsers(q);
    }

    // Adăugare prieten
    @PostMapping("/friends/add")
    public String addFriend(@RequestHeader("X-User-Name") String currentUsername,
                            @RequestParam String friendUsername) {
        userService.addFriend(currentUsername, friendUsername);
        return "Prieten adăugat!";
    }

    // Lista de prieteni
    @GetMapping("/friends")
    public Set<User> getFriends(@RequestHeader("X-User-Name") String username) {
        return userService.getFriends(username);
    }
}