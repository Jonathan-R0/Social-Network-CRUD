package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.RecoverPasswordRequestDto;

public interface PasswordResetService {
    void sendEmail(String email);

    void recoverPassword(Long userId, String token, RecoverPasswordRequestDto request);
}
