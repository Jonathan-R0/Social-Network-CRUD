package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import com.uba.ejercicio.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void followUser(Long followerId, Long followedId) {
        var follower = getUserById(followerId);
        var followed = getUserById(followedId);
        follower.getFollowing().add(followed);
        followed.getFollowers().add(follower);
        userRepository.save(follower);
        userRepository.save(followed);
    }


    public void unfollowUser(Long followerId, Long followedId) {
        var follower = getUserById(followerId);
        var followed = getUserById(followedId);
        follower.getFollowing().remove(followed);
        followed.getFollowers().remove(follower);
        userRepository.save(follower);
        userRepository.save(followed);
    }

    public List<String> getFollowers(User user) {
        return user.getFollowers().stream().map(User::getEmail).toList();
    }

    public List<String> getFollowing(User user) {
        return user.getFollowing().stream().map(User::getEmail).toList();
    }
}
