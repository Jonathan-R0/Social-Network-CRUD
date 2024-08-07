package com.uba.ejercicio.persistance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table
public class AccountValidationToken {
    @Id
    private Long userId;

    private String token;

    private Date expirationDate;
}
