package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
}
