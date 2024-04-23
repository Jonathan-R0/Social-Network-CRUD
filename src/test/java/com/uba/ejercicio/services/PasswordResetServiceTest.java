package com.uba.ejercicio.services;

import com.uba.ejercicio.configuration.MimeMessageHelperFactory;
import com.uba.ejercicio.dto.RecoverPasswordRequestDto;
import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.repositories.PasswordResetRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import com.uba.ejercicio.services.impl.EmailServiceImpl;
import com.uba.ejercicio.services.impl.PasswordResetServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PasswordResetServiceTest {

    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);

    private final MimeMessageHelperFactory mimeMessageHelperFactory = Mockito.mock(MimeMessageHelperFactory.class);

    @BeforeEach
    public void setUp() throws MessagingException {
        this.passwordResetService = new PasswordResetServiceImpl(userService, passwordResetRepository, passwordEncoder,
                new EmailServiceImpl(javaMailSender, mimeMessageHelperFactory));
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        when(javaMailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
        when(mimeMessageHelperFactory.create(any(MimeMessage.class), eq("utf-8")))
                .thenReturn(Mockito.mock(MimeMessageHelper.class));
        passwordResetRepository.deleteAll();
        userRepository.deleteAll();
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
        var token = passwordResetRepository.findAll().get(0);
        token.setExpirationDate(token.getExpirationDate().plusDays(1)); // Set expiration date to be in the future
        passwordResetRepository.save(token);
        var user = userRepository.findByEmail(email).orElseThrow();
        passwordResetService.recoverPassword(user.getId(), passwordResetRepository.findAll().get(0).getToken().toString(),
                RecoverPasswordRequestDto.builder().password(newPassword).build());
        Assertions.assertEquals(0, passwordResetRepository.count());
    }

}
