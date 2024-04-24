package com.uba.ejercicio.services.impl;


import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.UnavailableRoleException;
import com.uba.ejercicio.persistance.entities.ConfirmationToken;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.ConfirmationTokenRepository;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import com.uba.ejercicio.services.ConfirmationTokenService;
import com.uba.ejercicio.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
@Service
@NoArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {


    @Autowired
    protected ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void confirm(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        if (confirmationToken != null)
        {
            User user = confirmationToken.getUser();
            user.setConfirmated(true);
            userRepository.save(user);
        }
    }

}
