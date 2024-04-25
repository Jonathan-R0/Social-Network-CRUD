package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.dto.UserListDto;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.services.EmailService;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Value("${client.url}")
    private String clientUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto user) {
        User createdUser = userService.createUser(user);


        String link = clientUrl + "/user/validate?&token=" + createdUser.getConfirmationToken().getConfirmationToken();
        emailService.sendEmail(user.getEmail(), "Validar usuario", "Open this link to validate your mail: " + link);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/self")
    public ResponseEntity<Void> deleteUserByEmail(@RequestHeader("Authorization") String token) {
        userService.deleteUserByEmail(tokenService.getEmailFromHeader(token));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsersFromList(
            @Valid @RequestBody UserListDto users
        ) {
        userService.deleteAllFromList(users.getEmails());
        return ResponseEntity.ok().build();
    }

}
