package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    private static final int BEARER_LENGTH = "Bearer ".length();

    private void checkTokenHeader(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) throw new TokenException("Invalid token.");
    }


    private void checkValidatedEmail(String email) {
        User user = userService.getUserByEmail(email);
        if (!user.isConfirmated()) throw new TokenException("Invalid token.");
    }

    @PostMapping(value="/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto credentials) {
        LoginResponseDto login = tokenService.authResponse(credentials.getEmail(), credentials.getPassword());
        checkValidatedEmail(credentials.getEmail());
        return ResponseEntity.ok(login);
    }

    @PostMapping(value="/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestHeader("Authorization") String tokenHeader)
            throws TokenException {
        checkTokenHeader(tokenHeader);
        return ResponseEntity.ok(tokenService.refreshToken(tokenHeader.substring(BEARER_LENGTH)));
    }

    @PostMapping(value="/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String tokenHeader) {
        checkTokenHeader(tokenHeader);
        tokenService.destroySession(tokenService.getEmailFromHeader(tokenHeader));
        return ResponseEntity.ok().build();
    }

}
