package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.ChangePasswordDto;
import com.uba.ejercicio.dto.RecoverPasswordRequestDto;
import com.uba.ejercicio.dto.ResetPasswordRequestDto;
import com.uba.ejercicio.services.PasswordResetService;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/forgot")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
        passwordResetService.sendEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/recover/{token}")
    public ResponseEntity<Void> recoverPassword(
            @PathVariable("userId") Long userId,
            @PathVariable("token") String token,
            @RequestBody @Valid RecoverPasswordRequestDto request
    ) {
        passwordResetService.recoverPassword(userId, token, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordDto userDto,
            @RequestHeader("Authorization") String tokenHeader
    ) {
        userService.updatePassword(tokenService.getEmailFromHeader(tokenHeader),
                userDto.getOldPassword(), userDto.getNewPassword());
        return ResponseEntity.ok().build();
    }

}
