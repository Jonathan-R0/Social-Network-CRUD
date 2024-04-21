package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.ProfileResponseDto;
import com.uba.ejercicio.persistance.entities.Profile;

import java.util.Optional;

public interface ProfileService {

    Optional<Profile> getUserProfile(String userId);

    void createProfile(String userId, ProfileResponseDto profileInformation);

    void modifyProfile(String userId, ProfileResponseDto profileInformation);
}
