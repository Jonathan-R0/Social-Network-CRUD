package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    Optional<Gender> findByName(String name);

}
