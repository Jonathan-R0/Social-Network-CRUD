package com.uba.ejercicio.controllers;

import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/{userId}/followed")
public class FollowedController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<String>> getFollowed(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userService.getFollowing(user));
    }


    @DeleteMapping("/user/{followedUserId}")
    public ResponseEntity<Void> deleteFollowed(@PathVariable("userId") Long followerUserId, @PathVariable Long followedUserId) {
        userService.unfollowUser(followerUserId,followedUserId);
        return ResponseEntity.ok().build();

    }


}
