package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetUserByEmailDoesNotReturnAnythingWhenNoUsersArePresent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserByEmail("test"));
    }

    @Test
    public void testGetUserByEmailDoesReturnOneResultWhenOneUserIsPresent() {
        String exampleMail = "test@example.com";
        userService.createUser(UserDto.builder().email(exampleMail).password("password").build());
        User result = userService.getUserByEmail(exampleMail);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(exampleMail, result.getEmail());
    }

    @Test
    public void testCreateUserCreatesUser() {
        String exampleMail = "test@example.com";
        UserDto dto = UserDto.builder().email(exampleMail).password("password").build();
        User result = userService.createUser(dto);
        User fetchedData = userRepository.findByEmail(exampleMail).orElseThrow();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(fetchedData.getEmail(), result.getEmail());
    }

    @Test
    public void testDeleteUserByEmailDeletesUser() {
        String exampleMail = "test@example.com";
        UserDto dto = UserDto.builder().email(exampleMail).password("password").build();
        userService.createUser(dto);
        userService.deleteUserByEmail(exampleMail);
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserByEmail(exampleMail));
    }
    //Test para el método getUserById exitoso y fracaso
    @Test
    public void testGetUserByIdDoesReturnOneResultWhenOneUserIsPresent() {
        String exampleMail = "test@example.com";
        User user = userService.createUser(UserDto.builder().email(exampleMail).password("password").build());
        User result = userService.getUserById(user.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(exampleMail, result.getEmail());
    }
    @Test
    public void testGetUserByIdDoesNotReturnAnythingWhenNoUsersArePresent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserById(1L));
    }
    //Test para el método followUser exitoso y fracaso
    //Test para el método unfollowUser exitoso y fracaso
    //Test para el método getFollowers lista vacia y no vacia
}

