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

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        Profile profile = profileService.getUserProfile(userCheckMiddleware.getUserIdFromHeader(tokenHeader));
        return ResponseEntity.ok(profile.profileToDTO());
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> createProfile(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody @Valid ProfileResponseDto profileInformation
    ) {
        profileService.createProfile(userCheckMiddleware.getUserIdFromHeader(tokenHeader), profileInformation);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> modifyProfile(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestBody @Valid ProfileResponseDto profileInformation
    ) {
        profileService.modifyProfile(userCheckMiddleware.getUserIdFromHeader(tokenHeader), profileInformation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileResponseDto>> getProfiles() {
        return ResponseEntity.ok(profileService.getUserProfiles());
    }

}