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
        Optional<Profile> profile = profileService.getUserProfile(userId); //TODO throw error if doesnt exist

        if (profile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(profile.get().profileToDTO());
    }

    @PostMapping
    public ResponseEntity<Void> createProfile(@PathVariable String userId, @RequestBody @Valid ProfileResponseDto profileInformation) {
        profileService.createProfile(userId, profileInformation);

        return ResponseEntity.ok().build(); //TODO if profile not found qué hacemos?
    }

    @PutMapping
    public ResponseEntity<Void> modifyProfile(@PathVariable String userId, @RequestBody @Valid ProfileResponseDto profileInformation) {
        profileService.modifyProfile(userId, profileInformation);

        return ResponseEntity.ok().build();
    }
}