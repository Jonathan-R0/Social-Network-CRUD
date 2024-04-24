package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.*;
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

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto user) {
        userService.createUser(user);
        return ResponseEntity.ok().build();
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

    @PostMapping("/{userId}/follower")
    public ResponseEntity<Void> followUser(
            @PathVariable("userId") Long followerId,
            @RequestBody @Valid FollowRequestDto followRequest
        ) {
        userService.followUser(followerId, followRequest.getFollow());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/following")
    public ResponseEntity<Void> deleteFollowing(
            @PathVariable("userId") Long followerId,
            @RequestBody @Valid UnfollowRequestDto unfollowRequest
    ) {
        userService.unfollowUser(followerId, unfollowRequest.getUnfollow());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/follower")
    public ResponseEntity<Void> deleteFollower(
            @PathVariable("userId") Long followedId,
            @RequestBody @Valid UnfollowRequestDto unfollowRequest
    ) {
        userService.unfollowUser(unfollowRequest.getUnfollow(), followedId);
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
