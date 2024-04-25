package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.ConfirmationToken;
import com.uba.ejercicio.persistance.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface ConfirmationTokenService
{

    void confirm(String token);
}
