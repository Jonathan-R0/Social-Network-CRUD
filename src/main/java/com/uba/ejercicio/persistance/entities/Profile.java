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

    @OneToMany(fetch = FetchType.EAGER)
    private List<Hobby> hobbies;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ProfileResponseDto profileToDTO() {
        return ProfileResponseDto.builder()
                .name(name)
                .lastName(lastName)
                .gender(gender.getName())
                .birthDate(birthDate)
                .photo(photo)
                .hobbies(hobbies.stream().map(Hobby::getName).collect(Collectors.toList()))
                .build();
    }


}
