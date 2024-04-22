package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.LoginResponseDto;

public interface TokenService {

    LoginResponseDto refreshToken(String token);

    LoginResponseDto authResponse(String email, String password);

    String getEmailFromHeader(String header);

}
