package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
