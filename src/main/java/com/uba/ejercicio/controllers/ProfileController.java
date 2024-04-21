package com.uba.ejercicio.controllers;

import org.springframework.http.HttpStatus;
import com.uba.ejercicio.dto.ProfileResponseDto;
import com.uba.ejercicio.persistance.entities.Profile;
import com.uba.ejercicio.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/v1/user/{userId}/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable String userId) {
        Profile profile = profileService.getUserProfile(Long.parseLong(userId));

        return ResponseEntity.ok(profile.profileToDTO());
    }

    @PostMapping
    public ResponseEntity<Void> createProfile(@PathVariable String userId, @RequestBody @Valid ProfileResponseDto profileInformation) {
        profileService.createProfile(Long.parseLong(userId), profileInformation);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> modifyProfile(@PathVariable String userId, @RequestBody @Valid ProfileResponseDto profileInformation) {
        profileService.modifyProfile(Long.parseLong(userId), profileInformation); //TODO que hacemos si no lo encuentra

        return ResponseEntity.ok().build();
    }
}