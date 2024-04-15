package com.uba.ejercicio.services;

import com.uba.ejercicio.persistance.entities.Gender;
import com.uba.ejercicio.persistance.repositories.GenderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GenderServiceTest {

    @Autowired
    private GenderService genderService;

    @Autowired
    private GenderRepository genderRepository;

    @BeforeEach
    public void setUp() {
        genderRepository.deleteAll();
    }

    @Test
    public void testGetAllGendersShouldReturnNothingWhenEmpty() {
        Assertions.assertEquals(0, genderService.getAllGenders().size());
    }

    @Test
    public void testGetAllGendersShouldReturnOneValue() {
        String name = "example";
        genderRepository.save(Gender.builder().name(name).build());
        var genders = genderService.getAllGenders();
        Assertions.assertEquals(1, genders.size());
        Assertions.assertEquals(name, genders.get(0));
    }

    @Test
    public void testGetAllGendersShouldReturnThreeValues() {
        genderRepository.save(Gender.builder().name("example1").build());
        genderRepository.save(Gender.builder().name("example2").build());
        genderRepository.save(Gender.builder().name("example3").build());
        var genders = genderService.getAllGenders();
        Assertions.assertEquals(3, genders.size());
    }

    @Test
    public void testDeleteGenderShouldDeleteOneValue() {
        String name = "example";
        genderRepository.save(Gender.builder().name(name).build());
        genderService.deleteGender(List.of(name));
        Assertions.assertEquals(0, genderRepository.findAll().size());
    }

    @Test
    public void testDeleteGenderShouldDeleteAllValues() {
        genderRepository.save(Gender.builder().name("example1").build());
        genderRepository.save(Gender.builder().name("example2").build());
        genderRepository.save(Gender.builder().name("example3").build());
        genderService.deleteGender(List.of("example1", "example2", "example3"));
        Assertions.assertEquals(0, genderRepository.findAll().size());
    }

    @Test
    public void testCreateGenderIfNotExistsShouldCreateOne() {
        genderService.createGenderIfNotExists(List.of("example"));
        Assertions.assertEquals(1, genderRepository.findAll().size());
    }

    @Test
    public void testCreateGenderIfNotExistsShouldCreateThree() {
        genderService.createGenderIfNotExists(List.of("example1", "example2", "example3"));
        Assertions.assertEquals(3, genderRepository.findAll().size());
    }
}
