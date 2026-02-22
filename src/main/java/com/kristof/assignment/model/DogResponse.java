package com.kristof.assignment.model;

import com.kristof.assignment.db.entity.DogEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DogResponse {
    private String name;
    private String breed;
    private Gender gender;
    private String image;

    public static DogResponse fromEntity(DogEntity dogEntity){
        return DogResponse.builder()
                .name(dogEntity.getName())
                .breed(dogEntity.getBreed())
                .gender(dogEntity.getGender())
                .image(dogEntity.getImage())
                .build();
    }
}

