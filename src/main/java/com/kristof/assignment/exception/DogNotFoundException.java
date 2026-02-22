package com.kristof.assignment.exception;

public class DogNotFoundException extends RuntimeException{
    public DogNotFoundException(Long id) {
        super("Dog with id: " + id + " not found");
    }
}
