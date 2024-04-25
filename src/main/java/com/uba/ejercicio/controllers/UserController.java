package com.uba.ejercicio.controllers;

import com.uba.ejercicio.configuration.UserCheckMiddleware;
import com.uba.ejercicio.dto.*;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.services.TokenService;
import com.uba.ejercicio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCheckMiddleware userCheckMiddleware;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserDto user) {
        User createdUser = userService.createUser(user);
        tokenService.createAccountValidationTokenAndSendEmail(createdUser);
        return ResponseEntity.ok().body(createdUser.toUserResponseDto());
    }

    @DeleteMapping("/self")
    public ResponseEntity<Void> deleteUserByEmail(@RequestHeader("Authorization") String token) {
        userService.deleteUserByEmail(tokenService.getEmailFromHeader(token));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsersFromList(
            @Valid @RequestBody UserListDto users
        ) {
        userService.deleteAllFromList(users.getEmails());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/follower")
    public ResponseEntity<Void> followUser(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody @Valid FollowRequestDto followRequest
        ) {
        userService.followUser(userCheckMiddleware.getUserIdFromHeader(tokenHeader), followRequest.getFollow());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/following")
    public ResponseEntity<Void> deleteFollowing(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody @Valid UnfollowRequestDto unfollowRequest
    ) {
        userService.unfollowUser(userCheckMiddleware.getUserIdFromHeader(tokenHeader), unfollowRequest.getUnfollow());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follower")
    public ResponseEntity<Void> deleteFollower(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody @Valid UnfollowRequestDto unfollowRequest
    ) {
        userService.unfollowUser(unfollowRequest.getUnfollow(), userCheckMiddleware.getUserIdFromHeader(tokenHeader));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/follower")
    public ResponseEntity<FollowResponseDto> getFollower(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok()
                             .body(FollowResponseDto.builder()
                                    .follows(userService.getFollowers(userService.getUserById(userId)))
                             .build());
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<FollowResponseDto> getFollowing(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok()
                             .body(FollowResponseDto.builder()
                                    .follows(userService.getFollowing(userService.getUserById(userId)))
                             .build());
    }

}
