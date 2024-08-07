package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.AccountValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountValidationTokenRepository extends JpaRepository<AccountValidationToken, Long>{
}
