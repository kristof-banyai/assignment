package com.kristof.assignment.db.repository;

import com.kristof.assignment.db.entity.DogEntity;
import com.kristof.assignment.model.Gender;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@NoArgsConstructor
public final class DogSpecifications {

    public static Specification<DogEntity> hasBreedIgnoreCase(String breed) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("breed")), breed.toLowerCase());
    }

    public static Specification<DogEntity> hasGender(Gender gender) {
        return (root, query, cb) -> cb.equal(root.get("gender"), gender);
    }

    public static Specification<DogEntity> nameContainsIgnoreCase(String name) {
        String pattern = "%" + name.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }
}