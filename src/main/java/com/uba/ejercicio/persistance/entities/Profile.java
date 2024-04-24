package com.uba.ejercicio.persistance.entities;

import com.uba.ejercicio.dto.ProfileResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String lastName;

    @Lob
    private byte[] photo;

    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @OneToMany
    private List<Hobby> hobbies; // TODO delete hobbies when deleting profile

    @OneToOne
    private User user;

    public ProfileResponseDto profileToDTO() {
        return ProfileResponseDto.builder()
                .name(name)
                .lastName(lastName)
                .gender(gender.getName())
                .birthDate(birthDate)
                .hobbies(hobbies.stream().map(Hobby::getName).collect(Collectors.toList()))
                .build();
    }


}
