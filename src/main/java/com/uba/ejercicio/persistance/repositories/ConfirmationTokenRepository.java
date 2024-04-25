package com.uba.ejercicio.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uba.ejercicio.persistance.entities.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    ConfirmationToken findByUserId(Long userId);
 }
