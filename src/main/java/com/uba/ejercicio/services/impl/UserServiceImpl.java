package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(UserDto user) {
        return userRepository.save(
                User.builder()
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build()
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public void deleteUserByEmail(String email) {
        userRepository.delete(getUserByEmail(email));
    }
}
