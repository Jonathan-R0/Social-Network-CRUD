package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByUserId(Long userId);
}
