package com.kristof.assignment.service;

import com.kristof.assignment.db.entity.DogEntity;
import com.kristof.assignment.db.repository.DogRepository;
import com.kristof.assignment.db.repository.DogSpecifications;
import com.kristof.assignment.exception.DogNotFoundException;
import com.kristof.assignment.model.DogRequest;
import com.kristof.assignment.model.DogResponse;
import com.kristof.assignment.model.Gender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;

    public List<DogResponse> getAllDogs(String name, String breed, String gender) {
        List<Specification<DogEntity>> specs = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            specs.add(DogSpecifications.nameContainsIgnoreCase(name));
        }
        if (breed != null && !breed.isBlank()) {
            specs.add(DogSpecifications.hasBreedIgnoreCase(breed));
        }
        if (gender != null && !gender.isBlank()) {
            Gender parsedGender;
            try {
                parsedGender = Gender.valueOf(gender.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid gender. Allowed values: MALE, FEMALE");
            }
            specs.add(DogSpecifications.hasGender(parsedGender));
        }

        Specification<DogEntity> spec = Specification.allOf(specs);

        return dogRepository.findAll(spec).stream()
                .map(DogResponse::fromEntity)
                .toList();
    }

    public DogResponse getDogById(Long id) {
        return DogResponse.fromEntity(dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id)));
    }

    @Transactional
    public DogResponse createDog(DogRequest dogRequest) {
        DogEntity dogEntity = DogEntity.fromRequest(dogRequest);
        return DogResponse.fromEntity(dogRepository.save(dogEntity));
    }

    @Transactional
    public String deleteDogById(Long id) {
        if (!dogRepository.existsById(id))
            throw new DogNotFoundException(id);
        dogRepository.deleteById(id);
        return "Dog with id: " + id + " deleted";
    }
}
