package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDto user) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public void deleteUserByEmail(String email) {

    }
}
