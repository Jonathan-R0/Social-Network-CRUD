package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.UserDto;
import com.uba.ejercicio.exceptions.UnavailableRoleException;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.RefreshTokenRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final Set<String> VALID_ROLES = Set.of("ADMIN", "USER");

    @Override
    public User createUser(UserDto user) {
        if (!VALID_ROLES.contains(user.getRole())) throw new UnavailableRoleException(user.getRole());
        return userRepository.save(
                User.builder()
                    .email(user.getEmail())
                    .role(user.getRole())
                    .follows(new ArrayList<>())
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
        deleteUserTokensAndUserFromEmail(email);
    }

    @Override
    public void deleteAllFromList(List<String> emails) {
        emails.forEach(this::deleteUserTokensAndUserFromEmail);
    }

    @Override
    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = getUserByEmail(email);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            updateUser(user);
        } else {
            throw new IllegalArgumentException("Old password is incorrect."); // TODO: Create custom exception
        }
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    private void deleteUserTokensAndUserFromEmail(String email) {
        refreshTokenRepository.deleteById(email);
        userRepository.delete(getUserByEmail(email));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(); // TODO Create custom exception
    }

    @Override
    public void followUser(Long followerId, Long followedId) {
        var follower = getUserById(followerId);
        var followed = getUserById(followedId);
        if (!follower.getFollows().contains(followed)) {
            follower.getFollows().add(followed);
            userRepository.save(follower);
        }
    }

    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        var follower = getUserById(followerId);
        var followed = getUserById(followedId);
        follower.getFollows().remove(followed);
        userRepository.save(follower);
    }

    @Override
    public List<String> getFollowers(User user) {
        return userRepository.getFollowers(user.getId()).stream().map(User::getEmail).toList();
    }

    @Override
    public List<String> getFollowing(User user) {
        return user.getFollows().stream().map(User::getEmail).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = getUserByEmail(email);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole()));

        return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(),
                true, true, true, true, authorities);

    }
}
