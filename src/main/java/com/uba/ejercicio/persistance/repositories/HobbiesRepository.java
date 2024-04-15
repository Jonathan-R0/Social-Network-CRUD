package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbiesRepository extends JpaRepository<Hobby, Long>{
}
