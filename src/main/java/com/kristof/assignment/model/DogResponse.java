package com.kristof.assignment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DogResponse {
    private String name;
    private String breed;
    private Gender gender;
    private String image;
}