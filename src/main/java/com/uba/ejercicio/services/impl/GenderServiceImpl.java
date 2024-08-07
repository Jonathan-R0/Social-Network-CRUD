package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.persistance.entities.Gender;
import com.uba.ejercicio.persistance.repositories.GenderRepository;
import com.uba.ejercicio.services.GenderService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class GenderServiceImpl implements GenderService {

    @Autowired
    private GenderRepository genderRepository;

    public void createGenderIfNotExists(List<String> genders) {
        genders.forEach(gender ->
            genderRepository.findByName(gender)
                            .orElseGet(() -> genderRepository.save(
                                    Gender.builder()
                                          .name(gender)
                                          .build()
                            ))
        );
    }

    public List<String> getAllGenders() {
        return genderRepository.findAll()
                               .stream()
                               .map(Gender::getName)
                               .toList();
    }

    public void deleteGender(List<String> genders) {
        genders.forEach(gender -> genderRepository.findByName(gender).ifPresent(genderRepository::delete));
    }

}
