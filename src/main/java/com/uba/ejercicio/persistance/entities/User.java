package com.uba.ejercicio.persistance.entities;

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

    @OneToOne
    private Profile profile;

    @ManyToMany
    private Set<User> followers;

    @ManyToMany
    private Set<User> following;

}
