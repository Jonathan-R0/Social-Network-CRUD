package com.uba.ejercicio.persistance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
@AllArgsConstructor
public class RefreshToken {

    private String token;

    @Id
    private String email;

    private LocalDateTime creationTime;

}
