package com.uba.ejercicio.configuration;

import com.uba.ejercicio.exceptions.TokenException;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCheckMiddleware {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    public void checkTokenHeader(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) throw new TokenException("Invalid token.");
    }

    public void checkUser(Long userId, String tokenHeader) {
        checkTokenHeader(tokenHeader);
        String emailFromToken = tokenService.getEmailFromHeader(tokenHeader);
        String email = userService.getUserById(userId).getEmail();
        if (!email.equals(emailFromToken))
            throw new TokenException("Invalid user.");
    }

}
