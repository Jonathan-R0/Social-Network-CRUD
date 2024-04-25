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

    public Long getUserIdFromHeader(String tokenHeader) {
        checkTokenHeader(tokenHeader);
        return userService.getUserByEmail(tokenService.getEmailFromHeader(tokenHeader)).getId();
    }

}
