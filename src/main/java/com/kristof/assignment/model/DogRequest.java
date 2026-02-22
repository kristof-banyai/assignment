package com.kristof.assignment.model;
import com.kristof.assignment.model.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DogRequest {

    @NotBlank( message = "Name cannot be empty")
    private String name;
    @NotBlank( message = "Breed cannot be empty")
    private String breed;
    @NotNull( message = "Gender cannot be empty")
    private Gender gender;

    private String image;
}
