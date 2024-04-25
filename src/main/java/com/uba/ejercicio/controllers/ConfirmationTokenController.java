package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.ConfirmationTokenDto;
import com.uba.ejercicio.services.ConfirmationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user/confirmation")
public class ConfirmationTokenController {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @PostMapping
    public ResponseEntity<Void> createConfirmation(@RequestBody @Valid ConfirmationTokenDto user) {
        confirmationTokenService.confirm(user.getConfirmationToken());
        return ResponseEntity.ok().build();
    }

}
