package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
