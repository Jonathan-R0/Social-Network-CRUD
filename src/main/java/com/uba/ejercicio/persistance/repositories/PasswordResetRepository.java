package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID>{
}
