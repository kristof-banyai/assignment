package com.kristof.assignment.service;

import com.kristof.assignment.db.entity.DogEntity;
import com.kristof.assignment.db.repository.DogRepository;
import com.kristof.assignment.exception.DogNotFoundException;
import com.kristof.assignment.model.DogRequest;
import com.kristof.assignment.model.DogResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;

    public List<DogResponse> getAllDogs(){
        return dogRepository.findAll().stream()
                .map(DogResponse::fromEntity)
                .toList();
    }

    public DogResponse getDogById(Long id){
        return DogResponse.fromEntity(dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id)));
    }

    @Transactional
    public DogResponse createDog(DogRequest dogRequest){
        DogEntity dogEntity = DogEntity.fromRequest(dogRequest);
        return DogResponse.fromEntity(dogRepository.save(dogEntity));
    }

    @Transactional
    public String deleteDogById(Long id){
        if (!dogRepository.existsById(id))
            throw new DogNotFoundException(id);
        dogRepository.deleteById(id);
        return "Dog with id: " + id + " deleted";
        }
}
