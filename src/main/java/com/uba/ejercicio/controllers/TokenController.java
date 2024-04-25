package com.uba.ejercicio.controllers;

import com.uba.ejercicio.configuration.UserCheckMiddleware;
import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCheckMiddleware userCheckMiddleware;

    private static final int BEARER_LENGTH = "Bearer ".length();

    @PostMapping(value="/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto credentials) {
        return ResponseEntity.ok(tokenService.authResponse(credentials.getEmail(), credentials.getPassword()));
    }

    @PostMapping(value="/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestHeader("Authorization") String tokenHeader)
            throws TokenException {
        userCheckMiddleware.checkTokenHeader(tokenHeader);
        return ResponseEntity.ok(tokenService.refreshToken(tokenHeader.substring(BEARER_LENGTH)));
    }

    @PostMapping(value="/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String tokenHeader) {
        userCheckMiddleware.checkTokenHeader(tokenHeader);
        tokenService.destroySession(tokenService.getEmailFromHeader(tokenHeader));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value="/user/{userId}/validation/{token}")
    public ResponseEntity<Void> validate(
            @PathVariable("userId") Long userId,
            @PathVariable("token") String token) {
        tokenService.validateAccount(userId, token);
        return ResponseEntity.ok().build();
    }

}
