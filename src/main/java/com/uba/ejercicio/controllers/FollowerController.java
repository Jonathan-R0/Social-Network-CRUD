package com.uba.ejercicio.controllers;

import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/{userId}/follower")
public class FollowerController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<String>> getFollowers(@PathVariable("userId") Long userId){
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userService.getFollowers(user));
    }

    @PostMapping("/user/{followedUserId}")
    public ResponseEntity<Void> createFollower(@PathVariable("userId") Long followerUserId, @PathVariable Long followedUserId) {
        User followedUser = userService.getUserById(followedUserId);
        User followerUser = userService.getUserById(followerUserId);
        userService.followUser(followerUser,followedUser);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/user/{followedUserId}")
    public ResponseEntity<Void> deleteFollower(@PathVariable("userId") Long followerUserId, @PathVariable Long followedUserId) {
        User followedUser = userService.getUserById(followedUserId);
        User followerUser = userService.getUserById(followerUserId);
        userService.unfollowUser(followerUser,followedUser);
        return ResponseEntity.ok().build();

    }


}
