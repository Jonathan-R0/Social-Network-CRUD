package com.uba.ejercicio.controllers;

import com.uba.ejercicio.configuration.UserCheckMiddleware;
import com.uba.ejercicio.dto.ProfileResponseDto;
import com.uba.ejercicio.persistance.entities.Profile;
import com.uba.ejercicio.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/user")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserCheckMiddleware userCheckMiddleware;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable Long userId
    ) {
        userCheckMiddleware.checkUser(userId, tokenHeader);
        Profile profile = profileService.getUserProfile(userId);
        return ResponseEntity.ok(profile.profileToDTO());
    }

    @PostMapping("/{userId}/profile")
    public ResponseEntity<Void> createProfile(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable Long userId,
            @RequestBody @Valid ProfileResponseDto profileInformation
    ) {
        userCheckMiddleware.checkUser(userId, tokenHeader);
        profileService.createProfile(userId, profileInformation);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<Void> modifyProfile(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable Long userId,
            @RequestBody @Valid ProfileResponseDto profileInformation
    ) {
        userCheckMiddleware.checkUser(userId, tokenHeader);
        profileService.modifyProfile(userId, profileInformation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<List<ProfileResponseDto>> getProfiles() {
        return ResponseEntity.ok(profileService.getUserProfiles());
    }

}