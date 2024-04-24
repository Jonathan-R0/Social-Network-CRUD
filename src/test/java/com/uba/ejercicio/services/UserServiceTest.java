package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.UnavailableRoleException;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setUp() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    private static final Set<String> VALID_ROLES = Set.of("ADMIN", "USER");

    @Test
    public void testGetUserByEmailDoesNotReturnAnythingWhenNoUsersArePresent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserByEmail("test"));
    }

    @Test
    public void testGetUserByEmailDoesReturnOneResultWhenOneUserIsPresent() {
        String exampleMail = "test@example.com";
        userService.createUser(UserDto.builder().email(exampleMail).password("password").role("ADMIN").build());
        User result = userService.getUserByEmail(exampleMail);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(exampleMail, result.getEmail());
    }

    @Test
    public void testCreateUserCreatesUser() {
        String exampleMail = "test@example.com";
        UserDto dto = UserDto.builder().email(exampleMail).password("password").role("ADMIN").build();
        User result = userService.createUser(dto);
        User fetchedData = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(fetchedData.getEmail(), result.getEmail());
    }

    @Test
    public void testDeleteUserByEmailDeletesUser() {
        String exampleMail = "test@example.com";
        UserDto dto = UserDto.builder().email(exampleMail).password("password").role("ADMIN").build();
        userService.createUser(dto);
        userService.deleteUserByEmail(exampleMail);
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserByEmail(exampleMail));
    }

    @Test
    public void testDeleteUserByEmailDeletesSession() {
        String exampleMail = "test@example.com";
        String password = "password";
        UserDto dto = UserDto.builder().email(exampleMail).password(password).role("ADMIN").build();
        userService.createUser(dto);
        tokenService.authResponse(exampleMail, password);
        userService.deleteUserByEmail(exampleMail);
        Assertions.assertEquals(0, refreshTokenRepository.count());
    }

    @Test
    public void testThatAvailableRolesAreUsable() {
        VALID_ROLES.forEach(
            role -> {
                String email = role + "@example.com";
                UserDto dto = UserDto.builder()
                                     .email(email)
                                     .password("password")
                                     .role(role)
                                     .build();
                userService.createUser(dto);
                User result = userService.getUserByEmail(email);
                Assertions.assertNotNull(result);
                Assertions.assertEquals(role, result.getRole());
            }

        );
        Assertions.assertEquals(VALID_ROLES.size(), userRepository.count());
    }

    @Test
    public void testThatUnavailableRolesAreNotUsable() {
        Assertions.assertThrows(
                UnavailableRoleException.class,
                () -> userService.createUser(UserDto.builder()
                                                    .email("thebest@mail.lol")
                                                    .password("password")
                                                    .role("nope this is not a valid role")
                                                    .build())
        );
    }

    private void createUser(String email, String password) {
        userRepository.save(User.builder().email(email).password(passwordEncoder.encode(password)).role("USER").build());
    }

    @Test
    public void testThatDeleteAllFromListDeletesAllUsers() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        String email3 = "test3@test.com";
        String password = "password";
        createUser(email1, password);
        createUser(email2, password);
        createUser(email3, password);
        userService.deleteAllFromList(List.of(email1, email2, email3));
        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    public void testThatDeleteAllFromListDeletesOneUsers() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        String email3 = "test3@test.com";
        String password = "password";
        createUser(email1, password);
        createUser(email2, password);
        createUser(email3, password);
        userService.deleteAllFromList(List.of(email1));
        Assertions.assertEquals(2, userRepository.count());
    }

    @Test
    public void testThatDeleteAllFromListDeletesAllSessions() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        String email3 = "test3@test.com";
        String password = "password";
        createUser(email1, password);
        createUser(email2, password);
        createUser(email3, password);
        tokenService.authResponse(email1, password);
        tokenService.authResponse(email2, password);
        tokenService.authResponse(email3, password);
        userService.deleteAllFromList(List.of(email1, email2, email3));
        Assertions.assertEquals(0, refreshTokenRepository.count());
    }

    @Test
    public void testPasswordIsUpdatedWhenCredentialsAreCorrect() {
        String exampleMail = "test@example.com";
        String password = "password";
        UserDto dto = UserDto.builder().email(exampleMail).password(password).role("ADMIN").build();
        userService.createUser(dto);
        userService.updatePassword(exampleMail, password, "newPassword");
        User fetchedData = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertTrue(passwordEncoder.matches("newPassword", fetchedData.getPassword()));
    }

    @Test
    public void testPasswordIsNotUpdatedWhenCredentialsAreNotCorrect() {
        String exampleMail = "test@example.com";
        String password = "password";
        UserDto dto = UserDto.builder().email(exampleMail).password(password).role("ADMIN").build();
        userService.createUser(dto);
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.updatePassword(exampleMail, password + ".", "newPassword"));
    }


    @Test
    public void testValidateUser() {

        String exampleMail = "test@example.com";
        UserDto dto = UserDto.builder().email(exampleMail).password("password").role("ADMIN").build();
        User result = userService.createUser(dto);
        User fetchedData = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertFalse(fetchedData.isEnabled());
        userService.validateUser(exampleMail);
        User fetchedData2 = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertTrue(fetchedData2.isEnabled());
    }
}

