package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.RecoverPasswordRequestDto;
import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.repositories.PasswordResetRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
public class PasswordResetServiceTest {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        passwordResetRepository.deleteAll();
    }

    @Test
    public void testTokenIsSaved() {
        String email = "test@test.com";
        String password = "password";
        userService.createUser(UserDto.builder().email(email).password(password).role("USER").build());
        passwordResetService.sendEmail(email);
        Assertions.assertEquals(1, passwordResetRepository.count());
    }

    @Test
    public void testExceptionShouldBeThrownWhenEmailIsNotFound() {
        Assertions.assertThrows(NoSuchElementException.class, () -> passwordResetService.sendEmail("test@test.com"));
    }

    @Test
    public void testRecoverPasswordThrowsExceptionWhenTokenIsInvalid() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> passwordResetService.recoverPassword(1L, UUID.randomUUID().toString(),
                        RecoverPasswordRequestDto.builder().password("newPassword").build()));
    }

    @Test
    public void testRecoverPasswordThrowsExceptionWhenTokenIsExpired() {
        String email = "test@test.com";
        String password = "password";
        String newPassword = "newPassword";
        userService.createUser(UserDto.builder().email(email).password(password).role("USER").build());
        passwordResetService.sendEmail(email);
        var passwordResetToken = passwordResetRepository.findAll().get(0);
        passwordResetToken.setExpirationDate(
                passwordResetToken.getExpirationDate().minusDays(1)
        );
        passwordResetRepository.save(passwordResetToken);
        Assertions.assertThrows(RuntimeException.class,
                () -> passwordResetService.recoverPassword(1L,
                        passwordResetToken.getToken().toString(),
                        RecoverPasswordRequestDto.builder().password(newPassword).build()
                )
        );
    }

    @Test
    public void testRecoverPasswordThrowsExceptionWhenUserIdsDontMatch() {
        String email = "test@test.com";
        String password = "password";
        String newPassword = "newPassword";
        userService.createUser(UserDto.builder().email(email).password(password).role("USER").build());
        passwordResetService.sendEmail(email);
        Assertions.assertThrows(RuntimeException.class,
                () -> passwordResetService.recoverPassword(2L,
                        passwordResetRepository.findAll().get(0).getToken().toString(),
                        RecoverPasswordRequestDto.builder().password(newPassword).build()
                )
        );
    }

    @Test
    public void testRecoverPasswordResetsPasswordAndDeletesOldToken() {
        String email = "test@test.com";
        String password = "password";
        String newPassword = "newPassword";
        userService.createUser(UserDto.builder().email(email).password(password).role("USER").build());
        passwordResetService.sendEmail(email);
        passwordResetService.recoverPassword(1L, passwordResetRepository.findAll().get(0).getToken().toString(),
                RecoverPasswordRequestDto.builder().password(newPassword).build());
        Assertions.assertEquals(0, passwordResetRepository.count());
    }

}
