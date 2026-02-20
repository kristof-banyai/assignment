package com.kristof.assignment.db.entity;

import com.kristof.assignment.model.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dogs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
}