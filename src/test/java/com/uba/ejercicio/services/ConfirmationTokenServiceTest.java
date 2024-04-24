package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.UnavailableRoleException;
import com.uba.ejercicio.persistance.entities.ConfirmationToken;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.ConfirmationTokenRepository;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
public class ConfirmationTokenServiceTest {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    public void testValidateUser() {

        String exampleMail = "test@example.com";
        UserDto dto = UserDto.builder().email(exampleMail).password("password").role("ADMIN").build();
        User result = userService.createUser(dto);
        User fetchedData = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertFalse(fetchedData.isConfirmated());

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUserId(fetchedData.getId());
        confirmationTokenService.confirm(confirmationToken.getConfirmationToken());
        User fetchedData2 = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertTrue(fetchedData2.isConfirmated());
    }


}

