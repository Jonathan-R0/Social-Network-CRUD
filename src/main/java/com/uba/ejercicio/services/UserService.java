package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User createUser(UserDto user);

    User getUserByEmail(String email);

    void deleteUserByEmail(String email);

    void deleteAllFromList(List<String> emails);

    void updatePassword(String email, String oldPassword, String newPassword);

    void updateUser(User user);

    void validateUser(String email);
}
