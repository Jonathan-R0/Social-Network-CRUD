package com.uba.ejercicio.services;

import com.uba.ejercicio.configuration.TokenManager;
import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.persistance.entities.AccessToken;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.AccessTokenRepository;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void createUser(String email, String password) {
        userRepository.save(User.builder().email(email).password(passwordEncoder.encode(password)).role("USER").build());
    }

    @BeforeEach
    public void setUp() {
        refreshTokenRepository.deleteAll();
        accessTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testAuthenticationWorksWhenUserExists() {
        String email = "test@test.com";
        String password = "password";
        createUser(email, password);
        LoginResponseDto resp = tokenService.authResponse(email, password);
        Assertions.assertEquals(email, tokenManager.getEmailFromToken(resp.getAccessToken()));
    }

    @Test
    public void testAuthenticationWorksWhenManyUsersExists() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        String email3 = "test3@test.com";
        String password = "password";
        createUser(email1, password);
        createUser(email2, password);
        createUser(email3, password);
        LoginResponseDto resp = tokenService.authResponse(email2, password);
        Assertions.assertEquals(email2, tokenManager.getEmailFromToken(resp.getAccessToken()));
    }

    @Test
    public void testAuthenticationFailsWhenUserDoesNotExist() {
        Assertions.assertThrows(InternalAuthenticationServiceException.class,
                () -> tokenService.authResponse("test@test.com", "password"));
    }

    @Test
    public void testRefreshTokenThrowsExceptionWhenNoSessionWasCreated() {
        String token = tokenManager.generateToken(
                new org.springframework.security.core.userdetails.User("test@test.com", "password",
                true, true, true, true, new ArrayList<>())
        );
        Assertions.assertThrows(TokenException.class, () -> tokenService.refreshToken(token));
    }

    @Test
    public void testRefreshTokenWorksWhenSessionWasCreated() {
        String email = "test@test.com";
        String password = "password";
        createUser(email, password);
        LoginResponseDto resp = tokenService.authResponse(email, password);
        LoginResponseDto newResp = tokenService.refreshToken(resp.getRefreshToken());
        Assertions.assertEquals(email, tokenManager.getEmailFromToken(newResp.getAccessToken()));
        Assertions.assertEquals(newResp.getRefreshToken(),
                refreshTokenRepository.findByToken(newResp.getRefreshToken()).orElseThrow().getToken());
    }

    @Test
    public void testRefreshTokenWorksWhenManySessionsWereCreated() {
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        String email3 = "test3@test.com";
        String password = "password";
        createUser(email1, password);
        createUser(email2, password);
        createUser(email3, password);
        LoginResponseDto resp = tokenService.authResponse(email2, password);
        LoginResponseDto newResp = tokenService.refreshToken(resp.getRefreshToken());
        Assertions.assertEquals(email2, tokenManager.getEmailFromToken(newResp.getAccessToken()));
        Assertions.assertEquals(newResp.getRefreshToken(),
                refreshTokenRepository.findByToken(newResp.getRefreshToken()).orElseThrow().getToken());
    }

    @Test
    public void testAccessTokenIsReturnedWhenFoundInDatabase() {
        String email = "test@test.com";
        String password = "password";
        String token = "token";
        createUser(email, password);
        accessTokenRepository.save(new AccessToken(token, email, LocalDateTime.now()));
        LoginResponseDto resp = tokenService.authResponse(email, password);
        Assertions.assertEquals(token, resp.getAccessToken());
    }

    @Test
    public void testAccessTokenIsGeneratedWhenNotFoundInDatabase() {
        String email = "test@test.com";
        String password = "password";
        createUser(email, password);
        LoginResponseDto resp = tokenService.authResponse(email, password);
        Assertions.assertEquals(resp.getAccessToken(), accessTokenRepository.findById(email).orElseThrow().getToken());
    }

    @Test
    public void testAccessTokenIsChangedWhenRefreshTokenIsUsed() {
        String email = "test@test.com";
        String password = "password";
        createUser(email, password);
        LoginResponseDto initialCredentials = tokenService.authResponse(email, password);
        accessTokenRepository.deleteById(email);
        accessTokenRepository.save(
                new AccessToken(initialCredentials.getAccessToken(), email, LocalDateTime.now().minusDays(1))
        ); // Expired token, if not the same token will be generated
        LoginResponseDto newCredentials = tokenService.refreshToken(initialCredentials.getRefreshToken());
        Assertions.assertNotEquals(initialCredentials.getAccessToken(), newCredentials.getAccessToken());
        Assertions.assertEquals(1, accessTokenRepository.count());
    }
}
