package com.uba.ejercicio.persistance.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table
public class PasswordReset {

    @Id
    private UUID token;

    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;
}
