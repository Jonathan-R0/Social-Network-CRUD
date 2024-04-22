package com.uba.ejercicio.services;

import com.uba.ejercicio.dto.ProfileResponseDto;
import com.uba.ejercicio.exceptions.UserNotFoundException;
import com.uba.ejercicio.persistance.entities.Gender;
import com.uba.ejercicio.persistance.entities.Hobby;
import com.uba.ejercicio.persistance.entities.Profile;
import com.uba.ejercicio.persistance.entities.User;
import com.uba.ejercicio.persistance.repositories.GenderRepository;
import com.uba.ejercicio.persistance.repositories.HobbiesRepository;
import com.uba.ejercicio.persistance.repositories.ProfileRepository;
import com.uba.ejercicio.persistance.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = ALL)
public class ProfileServiceTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HobbiesRepository hobbiesRepository;


    @BeforeEach
    public void setUp() {
        profileRepository.deleteAll();
        genderRepository.deleteAll();
        userRepository.deleteAll();
        hobbiesRepository.deleteAll();
    }

    @Test
    public void testThrowsWhenUserDoesNotExist() throws Exception {
        Assertions.assertThrows(UserNotFoundException.class, () -> profileService.getUserProfile(1L));
    }

    @Test
    public void testCreateProfileWithExistingHobby() throws Exception { //TODO puede haber un usuario que no tenga un perfil creado?
        String exampleMail = "test@example.com";
        User user = User.builder().email(exampleMail).password("password").build();
        Hobby hobby = Hobby.builder().name("hobby").build();

        userRepository.save(user);
        genderRepository.save(Gender.builder().name("fem").build());
        hobbiesRepository.save(hobby);

        profileService.createProfile(user.getId(), ProfileResponseDto.builder()
                .name("Sara")
                .lastName("Jones")
                .gender("fem")
                .birthDate(new Date(2000, 1, 1))
                .hobbies(List.of("hobby"))
                .build());

        Optional<Profile> result = profileRepository.findProfileByUserId(user.getId());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Sara", result.get().getName());
        Assertions.assertEquals("Jones", result.get().getLastName());
        Assertions.assertEquals("fem", result.get().getGender().getName());
        Assertions.assertEquals(new Date(2000, 1, 1), result.get().getBirthDate());
        Assertions.assertEquals(1, result.get().getHobbies().size());
    }

    @Test
    public void testCreateProfileWithNoExistingHobby() throws Exception { //TODO puede haber un usuario que no tenga un perfil creado?
        String exampleMail = "test@example.com";
        User user = User.builder().email(exampleMail).password("password").build();
        Hobby hobby = Hobby.builder().name("hobby1").build();

        userRepository.save(user);
        genderRepository.save(Gender.builder().name("fem").build());
        hobbiesRepository.save(hobby);

        profileService.createProfile(user.getId(), ProfileResponseDto.builder()
                .name("Sara")
                .lastName("Jones")
                .gender("fem")
                .birthDate(new Date(2000, 1, 1))
                .hobbies(List.of("hobby1", "hobby2"))
                .build());

        Optional<Profile> result = profileRepository.findProfileByUserId(user.getId());
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Sara", result.get().getName());
        Assertions.assertEquals("Jones", result.get().getLastName());
        Assertions.assertEquals("fem", result.get().getGender().getName());
        Assertions.assertEquals(new Date(2000, 1, 1), result.get().getBirthDate());
        Assertions.assertEquals(2, result.get().getHobbies().size());
    }

    @Test
    public void testGetUserProfile() throws Exception { //TODO puede haber un usuario que no tenga un perfil creado?
        String exampleMail = "test@example.com";
        User user = User.builder().email(exampleMail).password("password").build();
        Hobby hobby = Hobby.builder().name("hobby1").build();

        userRepository.save(user);
        genderRepository.save(Gender.builder().name("fem").build());
        hobbiesRepository.save(hobby);

        profileService.createProfile(user.getId(), ProfileResponseDto.builder()
                .name("Sara")
                .lastName("Jones")
                .gender("fem")
                .birthDate(new Date(2000, 1, 1))
                .hobbies(List.of("hobby1", "hobby2"))
                .build());

        Profile result = profileService.getUserProfile(user.getId());

        Assertions.assertEquals("Sara", result.getName());
        Assertions.assertEquals("Jones", result.getLastName());
        Assertions.assertEquals("fem", result.getGender().getName());
        Assertions.assertEquals(new Date(2000, 1, 1), result.getBirthDate());
        Assertions.assertEquals(2, result.getHobbies().size());
    }

    @Test
    public void testModifyUserProfile() throws Exception { //TODO puede haber un usuario que no tenga un perfil creado?
        String exampleMail = "test@example.com";
        User user = User.builder().email(exampleMail).password("password").build();
        Hobby hobby = Hobby.builder().name("hobby1").build();

        userRepository.save(user);
        genderRepository.save(Gender.builder().name("fem").build());
        genderRepository.save(Gender.builder().name("other").build());
        hobbiesRepository.save(hobby);

        profileService.createProfile(user.getId(), ProfileResponseDto.builder()
                .name("Sara")
                .lastName("Jones")
                .gender("fem")
                .birthDate(new Date(2000, 1, 1))
                .hobbies(List.of("hobby1", "hobby2"))
                .build());

        profileService.modifyProfile(user.getId(), ProfileResponseDto.builder()
                .name("Luca")
                .lastName("Pedri")
                .gender("other")
                .birthDate(new Date(2000, 1, 2))
                .hobbies(List.of("hobby1"))
                .build());

        Profile result = profileService.getUserProfile(user.getId());

        Assertions.assertEquals("Luca", result.getName());
        Assertions.assertEquals("Pedri", result.getLastName());
        Assertions.assertEquals("other", result.getGender().getName());
        Assertions.assertEquals(new Date(2000, 1, 2), result.getBirthDate());
        Assertions.assertEquals(1, result.getHobbies().size());
    }
}
