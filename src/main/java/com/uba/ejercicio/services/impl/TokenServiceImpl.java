package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.configuration.TokenManager;
import com.uba.ejercicio.dto.LoginResponseDto;
import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.persistance.entities.RefreshToken;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Value("${jwt.refresh.duration}")
    private int refreshDuration;

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
        if (refreshTokenRepository.existsById(email)) {
            refreshTokenRepository.deleteById(email);
        }
        String refreshToken = tokenManager.generateRefreshToken(userDetails);
        refreshTokenRepository.save(new RefreshToken(refreshToken, email, LocalDateTime.now()));
        return new LoginResponseDto(token, refreshToken);
    }
}
