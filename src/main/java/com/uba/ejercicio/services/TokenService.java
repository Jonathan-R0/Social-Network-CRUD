package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.persistance.entities.User;

public interface TokenService {

    LoginResponseDto refreshToken(String token);

    LoginResponseDto authResponse(String email, String password);

    String getEmailFromHeader(String header);

    void destroySession(String email);

    void createAccountValidationTokenAndSendEmail(User user);

    void validateAccount(Long userId, String token);
}
