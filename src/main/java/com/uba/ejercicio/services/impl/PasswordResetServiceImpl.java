package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.RecoverPasswordRequestDto;
import com.uba.ejercicio.exceptions.PasswordResetException;
import com.uba.ejercicio.persistance.entities.PasswordReset;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.PasswordResetRepository;
import com.uba.ejercicio.services.EmailService;
import com.uba.ejercicio.services.PasswordResetService;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserService userService;

    private final PasswordResetRepository passwordResetRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public PasswordResetServiceImpl(
            UserService userService,
            PasswordResetRepository passwordResetRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userService = userService;
        this.passwordResetRepository = passwordResetRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Value("${password.reset.email.duration}")
    private int duration;

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void sendEmail(String email) {
        User user = userService.getUserByEmail(email);
        UUID token = UUID.randomUUID();
        PasswordReset passwordReset = PasswordReset.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusSeconds(duration))
                .build();
        String link = clientUrl + "password/reset?token=" + token + "&id=" + user.getId();
        emailService.sendEmail(email, "Password reset", "Open this link to reset your password: " + link);
        passwordResetRepository.save(passwordReset);
    }

    @Override
    public void recoverPassword(Long userId, String token, RecoverPasswordRequestDto request) {
        PasswordReset passwordReset = passwordResetRepository.findById(UUID.fromString(token))
                .orElseThrow(() -> new PasswordResetException("Invalid request"));
        if (passwordReset.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new PasswordResetException("Token expired");
        }
        User user = passwordReset.getUser();
        if (!user.getId().equals(userId)) {
            throw new PasswordResetException("Invalid token");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.updateUser(user);
        passwordResetRepository.delete(passwordReset);
        emailService.sendEmail(user.getEmail(), "Password reset successful!", "Password reset successful :)");
    }
}
