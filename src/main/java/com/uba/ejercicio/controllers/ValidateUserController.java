package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.ValidateUserDto;
import com.uba.ejercicio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user/validate")
public class ValidateUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Void> validateUser(@RequestBody @Valid ValidateUserDto user) {
        userService.validateUser(user.getEmail());
        return ResponseEntity.ok().build();
    }

}
