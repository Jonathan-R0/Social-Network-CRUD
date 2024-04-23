package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.Profile;
import com.uba.ejercicio.persistance.entities.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto user);

    User getUserByEmail(String email);

    void deleteUserByEmail(String email);

    User getUserById(Long id);

    void followUser(Long follower, Long followed);

    void unfollowUser(Long follower, Long followed);

    List<String> getFollowers(User user);

    List<String> getFollowing(User user);
}
