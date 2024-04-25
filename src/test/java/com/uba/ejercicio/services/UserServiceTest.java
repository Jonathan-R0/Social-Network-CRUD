package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.SelfFollowException;
import com.uba.ejercicio.exceptions.UnavailableRoleException;
import com.uba.ejercicio.exceptions.UserNotFoundException;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private HobbiesRepository hobbiesRepository;

    @BeforeEach
    public void setUp() {
        profileRepository.deleteAll();
        genderRepository.deleteAll();
        hobbiesRepository.deleteAll();
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
    public void testGetUserByIdDoesReturnOneResultWhenOneUserIsPresent() {
        String exampleMail = "test@example.com";
        User user = userService.createUser(
                UserDto.builder().email(exampleMail).password("password").role("ADMIN").build()
        );
        User result = userService.getUserById(user.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(exampleMail, result.getEmail());
    }
    @Test
    public void testGetUserByIdDoesNotReturnAnythingWhenNoUsersArePresent() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    private void createPairOfUsers(String email1, String email2) {
        userService.createUser(UserDto.builder().email(email1).password("password").role("ADMIN").build());
        userService.createUser(UserDto.builder().email(email2).password("password").role("ADMIN").build());
    }

    @Test
    public void testGetFollowersReturnsEmptyListWhenNoFollowers() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        createPairOfUsers(email1, email2);
        List<String> followers = userService.getFollowers(userService.getUserByEmail(email1));
        Assertions.assertTrue(followers.isEmpty());
    }

    @Test
    @Transactional
    public void testGetFollowersReturnsListWithOneFollow() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        createPairOfUsers(email1, email2);
        userService.followUser(userService.getUserByEmail(email1).getId(), userService.getUserByEmail(email2).getId());
        List<String> following = userService.getFollowing(userService.getUserByEmail(email1));
        List<String> followers = userService.getFollowers(userService.getUserByEmail(email2));
        Assertions.assertEquals(1, following.size());
        Assertions.assertEquals(1, followers.size());
    }

    @Test
    @Transactional
    public void testFollowUserWorks() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        createPairOfUsers(email1, email2);
        userService.followUser(userService.getUserByEmail(email1).getId(), userService.getUserByEmail(email2).getId());
        Assertions.assertEquals(1, userService.getFollowing(userService.getUserByEmail(email1)).size());
        Assertions.assertEquals(1, userService.getFollowers(userService.getUserByEmail(email2)).size());
    }

    @Test
    public void testFollowUserDoesNotWorkWhenFirstUserNotPresent() {
        userService.createUser(UserDto.builder().email("test1@test.com").password("password").role("ADMIN").build());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.followUser(2L, 1L));
    }

    @Test
    public void testFollowUserDoesNotWorkWhenSecondUserNotPresent() {
        userService.createUser(UserDto.builder().email("test1@test.com").password("password").role("ADMIN").build());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.followUser(1L, 2L));
    }

    @Test
    @Transactional
    public void testUnfollowUserWorks() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        createPairOfUsers(email1, email2);
        userService.followUser(userService.getUserByEmail(email1).getId(), userService.getUserByEmail(email2).getId());
        userService.unfollowUser(userService.getUserByEmail(email1).getId(), userService.getUserByEmail(email2).getId());
        Assertions.assertEquals(0, userService.getFollowing(userService.getUserByEmail(email1)).size());
        Assertions.assertEquals(0, userService.getFollowers(userService.getUserByEmail(email2)).size());
    }

    @Test
    public void testUnfollowUserDoesNotWorkWhenFirstUserNotPresent() {
        userService.createUser(UserDto.builder().email("test1@test.com").password("password").role("ADMIN").build());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.unfollowUser(2L, 1L));
    }

    @Test
    public void testUnfollowUserDoesNotWorkWhenSecondUserNotPresent() {
        userService.createUser(UserDto.builder().email("test1@test.com").password("password").role("ADMIN").build());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.unfollowUser(1L, 2L));
    }

    private void activateUser(String email) {
        User user = userService.getUserByEmail(email);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Test
    public void testDeleteUserByEmailDeletesSession() {
        String exampleMail = "test@example.com";
        String password = "password";
        UserDto dto = UserDto.builder().email(exampleMail).password(password).role("ADMIN").build();
        userService.createUser(dto);
        activateUser(exampleMail);
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
        activateUser(email1);
        activateUser(email2);
        activateUser(email3);
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
    public void testCantFollowYourself() {
        Assertions.assertThrows(SelfFollowException.class, () -> userService.followUser(1L, 1L));
    }

    @Test
    public void testCantUnfollowYourself() {
        Assertions.assertThrows(SelfFollowException.class, () -> userService.unfollowUser(1L, 1L));
    }
}

