package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.dto.ProfileResponseDto;
import com.uba.ejercicio.persistance.entities.Gender;
import com.uba.ejercicio.persistance.entities.Hobby;
import com.uba.ejercicio.persistance.entities.Profile;
import com.uba.ejercicio.persistance.repositories.GenderRepository;
import com.uba.ejercicio.persistance.repositories.HobbiesRepository;
import com.uba.ejercicio.persistance.repositories.ProfileRepository;
import com.uba.ejercicio.services.ProfileService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private HobbiesRepository hobbyRepository;

    @Override
    public Optional<Profile> getUserProfile(String userId) {
        return profileRepository.findProfileById(userId);
    }

    private List<Hobby> getHobbies(List<String> dtoHobbies) {
        return dtoHobbies.stream()
                .map(hobbyName -> hobbyRepository.findByName(hobbyName)
                        .orElseGet(() -> {
                            Hobby newHobby = new Hobby();
                            newHobby.setName(hobbyName);
                            hobbyRepository.save(newHobby);
                            return newHobby;
                        }))
                .collect(Collectors.toList());
    }

    @Override
    public void createProfile(String userId, ProfileResponseDto profileInformation) {
        profileRepository.findProfileById(userId).ifPresentOrElse(
                profile -> {/* Do nothing if profile is found */},
                () -> {
                    Gender gender = genderRepository.findByName(profileInformation.getGender()).stream().findFirst().orElse(null);
                    profileRepository.save(Profile.builder()
                            .name(profileInformation.getName())
                            .lastName(profileInformation.getLastName())
                            .gender(gender)
                            .birthDate(profileInformation.getBirthDate())
                            .hobbies(getHobbies(profileInformation.getHobbies()))
                            .build());
                }
        );
    }

    @Override
    public void modifyProfile(String userId, ProfileResponseDto profileInformation) {
        profileRepository.findProfileById(userId).ifPresent(profile -> {
            Gender gender = genderRepository.findByName(profileInformation.getGender()).stream().findFirst().orElse(null);
            profile.setName(profileInformation.getName());
            profile.setLastName(profileInformation.getLastName());
            profile.setGender(gender);
            profile.setBirthDate(profileInformation.getBirthDate());
            profile.setHobbies(getHobbies(profileInformation.getHobbies()));
            profileRepository.save(profile);
        });
    }
}
