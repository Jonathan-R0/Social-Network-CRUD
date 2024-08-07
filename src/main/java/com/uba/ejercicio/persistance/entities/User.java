package com.uba.ejercicio.persistance.entities;

import com.uba.ejercicio.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    @ManyToMany
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private List<User> follows; // follower -> followed

    private Boolean enabled;

    public UserResponseDto toUserResponseDto() {
        return UserResponseDto.builder()
                .email(email)
                .role(role)
                .id(id)
                .build();
    }
}
