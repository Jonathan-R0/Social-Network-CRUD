package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.ConfirmationToken;
import com.uba.ejercicio.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
}
