package com.kristof.assignment.db.entity;

import com.kristof.assignment.model.DogRequest;
import com.kristof.assignment.model.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dogs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String breed;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String image;

    public static DogEntity fromRequest(DogRequest dogRequest){
        return DogEntity.builder()
                .name(dogRequest.getName())
                .breed(dogRequest.getBreed())
                .gender(dogRequest.getGender())
                .image(dogRequest.getImage())
                .build();
    }
}