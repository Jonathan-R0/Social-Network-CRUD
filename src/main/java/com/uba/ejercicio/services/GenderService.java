package com.uba.ejercicio.services;

import java.util.List;

public interface GenderService {
    void createGenderIfNotExists(List<String> genders);

    List<String> getAllGenders();

    void deleteGender(List<String> genders);

}
