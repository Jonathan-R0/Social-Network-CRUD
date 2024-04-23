package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.RecoverPasswordRequestDto;
import com.uba.ejercicio.persistance.entities.PasswordReset;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.PasswordResetRepository;
import com.uba.ejercicio.services.PasswordResetService;
import com.uba.ejercicio.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Value("${password.reset.email.duration}")
    private int duration;

    @Value("${client.url}")
    private String clientUrl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendEmail(String email) {
        User user = userService.getUserByEmail(email);
        UUID token = UUID.randomUUID();
        PasswordReset passwordReset = PasswordReset.builder()
                .token(token)
                .user(user)
                .expirationDate(LocalDateTime.now().plusSeconds(duration))
                .build();
        String link = clientUrl + "/password/reset?token=" + token + "&id=" + user.getId();

        // TODO send email
        log.info("Password reset link: {}", link);

        passwordResetRepository.save(passwordReset);
    }

    @Override
    public void recoverPassword(Long userId, String token, RecoverPasswordRequestDto request) {
        PasswordReset passwordReset = passwordResetRepository.findById(UUID.fromString(token))
                .orElseThrow(); // TODO create exception for this case
        if (passwordReset.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired"); // TODO create exception for this case
        }
        User user = passwordReset.getUser();
        if (!user.getId().equals(userId)) {
            throw new RuntimeException("Invalid token"); // TODO create exception for this case
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.updateUser(user);
        passwordResetRepository.delete(passwordReset);

        // TODO send notification email
    }
}
