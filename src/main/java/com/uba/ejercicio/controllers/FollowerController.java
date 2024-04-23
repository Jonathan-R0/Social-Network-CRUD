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
        userService.followUser(followerUserId,followedUserId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/user/{followedUserId}")
    public ResponseEntity<Void> deleteFollower(@PathVariable("userId") Long followerUserId, @PathVariable Long followedUserId) {
        userService.unfollowUser(followerUserId,followedUserId);
        return ResponseEntity.ok().build();

    }


}
