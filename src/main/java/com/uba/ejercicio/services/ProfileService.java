package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.ProfileResponseDto;
import com.uba.ejercicio.persistance.entities.Profile;


public interface ProfileService {

    Profile getUserProfile(Long userId);

    void createProfile(Long userId, ProfileResponseDto profileInformation);

    void modifyProfile(Long userId, ProfileResponseDto profileInformation);
}
