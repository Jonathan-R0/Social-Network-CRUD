package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
