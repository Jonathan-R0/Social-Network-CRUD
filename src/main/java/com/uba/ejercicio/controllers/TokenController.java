package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    private static final int BEARER_LENGTH = "Bearer ".length();

    @PostMapping(value="/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto credentials) {
        return ResponseEntity.ok(tokenService.authResponse(credentials.getEmail(), credentials.getPassword()));
    }

    @PostMapping(value="/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestHeader("Authorization") String tokenHeader)
            throws TokenException {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) throw new TokenException("Invalid token.");
        return ResponseEntity.ok(tokenService.refreshToken(tokenHeader.substring(BEARER_LENGTH)));
    }

}
