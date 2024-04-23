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

    void followUser(User follower, User followed);

    void unfollowUser(User follower, User followed);

    List<String> getFollowers(User user);

    List<String> getFollowing(User user);
}
