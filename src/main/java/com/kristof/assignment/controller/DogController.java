package com.kristof.assignment.controller;

import com.kristof.assignment.model.DogRequest;
import com.kristof.assignment.model.DogResponse;
import com.kristof.assignment.service.DogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dogs")
@RequiredArgsConstructor
@Slf4j
public class DogController {

    private final DogService dogService;

    @GetMapping
    public List<DogResponse> getAllDogs(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String breed,
                                        @RequestParam(required = false) String gender
    ){
        log.info("Getting all dogs...");
        return dogService.getAllDogs(name, breed, gender);
    }

    @GetMapping("/{id}")
    public DogResponse getDogById(@PathVariable Long id){
        log.info("Getting dog with id: {}", id);
        return dogService.getDogById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DogResponse createDog(@RequestBody @Valid DogRequest dogRequest){
        return dogService.createDog(dogRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteDogById(@PathVariable Long id){
        return dogService.deleteDogById(id);
    }
}
