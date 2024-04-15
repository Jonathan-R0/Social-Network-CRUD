package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.GenderCreationDto;
import com.uba.ejercicio.services.GenderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/gender")
public class GenderController {

    @Autowired
    private GenderService genderService;

    @GetMapping
    public ResponseEntity<List<String>> getAllGenders() {
        return ResponseEntity.ok(genderService.getAllGenders());
    }

    @PostMapping
    public ResponseEntity<Void> createGender(@RequestBody @Valid GenderCreationDto genders) {
        genderService.createGenderIfNotExists(genders.getGenders());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteGender(@RequestBody @Valid GenderCreationDto genders) {
        genderService.deleteGender(genders.getGenders());
        return ResponseEntity.ok().build();
    }

}
