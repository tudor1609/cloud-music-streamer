package com.musicapp.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore // Parola nu trebuie trimisă niciodată spre frontend
    private String password;

    private String currentSongTitle;
    private String currentSongArtist;
    private LocalDateTime lastSeen;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore // Previne bucla infinită la serializarea JSON (Tedi -> Teditzu -> Tedi...)
    private Set<User> friends = new HashSet<>();

    // REPARARE CRITICĂ: equals bazat doar pe ID pentru a evita StackOverflow
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    // REPARARE CRITICĂ: hashCode constant sau bazat pe ID
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}