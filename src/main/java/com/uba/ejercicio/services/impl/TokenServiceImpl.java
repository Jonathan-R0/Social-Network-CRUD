package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.configuration.TokenManager;
import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.persistance.entities.AccountValidationToken;
import com.uba.ejercicio.persistance.entities.RefreshToken;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.AccountValidationTokenRepository;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.services.EmailService;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountValidationTokenRepository accountValidationTokenRepository;

    @Autowired
    private EmailService emailService;

    @Value("${client.url}")
    private String clientUrl;

    @Value("${jwt.access.duration}")
    private int accessDuration;

    @Value("${jwt.refresh.duration}")
    private int refreshDuration;

    private static final int BEARER_LENGTH = "Bearer ".length();

    @Override
    public LoginResponseDto refreshToken(String token) {
        var email = tokenManager.getEmailFromToken(token);
        RefreshToken foundToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("No refresh token was found"));
        if (!foundToken.getEmail().equals(email)) throw new TokenException("Invalid token");
        UserDetails userDetails = userService.loadUserByUsername(email);
        String newAccessToken = tokenManager.generateToken(userDetails);
        if (LocalDateTime.now().isAfter(foundToken.getCreationTime().plusSeconds(refreshDuration))) { // Restart session
            String newRefreshToken = tokenManager.generateRefreshToken(userDetails);
            foundToken.setToken(newRefreshToken);
            foundToken.setCreationTime(LocalDateTime.now());
            refreshTokenRepository.save(foundToken);
        }
        return new LoginResponseDto(newAccessToken, foundToken.getToken());
    }

    @Override
    public LoginResponseDto authResponse(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = userService.loadUserByUsername(email);
        String token = tokenManager.generateToken(userDetails);
        if (refreshTokenRepository.existsById(email)) refreshTokenRepository.deleteById(email);
        String refreshToken = tokenManager.generateRefreshToken(userDetails);
        refreshTokenRepository.save(new RefreshToken(refreshToken, email, LocalDateTime.now()));
        return new LoginResponseDto(token, refreshToken);
    }

    @Override
    public String getEmailFromHeader(String header) {
        return tokenManager.getEmailFromToken(header.substring(BEARER_LENGTH));
    }

    @Override
    public void destroySession(String email) {
        refreshTokenRepository.deleteById(email);
    }

    @Override
    public void createAccountValidationTokenAndSendEmail(User user) {
        UUID token = UUID.randomUUID();
        accountValidationTokenRepository.save(
                AccountValidationToken.builder()
                        .userId(user.getId())
                        .token(token.toString())
                        .expirationDate(new Date(System.currentTimeMillis() + accessDuration * 1000L))
                        .build()
        );
        String link = clientUrl + "auth/validate-account?token=" + token + "&id=" + user.getId();
        emailService.sendEmail(user.getEmail(), "Validate your account",
                "Click here to activate your account: " + link);
    }

    @Override
    public void validateAccount(Long userId, String token) {
        AccountValidationToken accountValidationToken = accountValidationTokenRepository.findById(userId)
                .orElseThrow(() -> new TokenException("User invalid or not found"));
        if (!accountValidationToken.getToken().equals(token)) throw new TokenException("Invalid token");
        if (accountValidationToken.getExpirationDate().before(new Date())) throw new TokenException("Token expired");
        accountValidationTokenRepository.delete(accountValidationToken);
        userService.validateAccount(userId);
    }
}
