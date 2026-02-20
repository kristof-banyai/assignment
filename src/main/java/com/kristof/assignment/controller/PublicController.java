package com.kristof.assignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicController {

    private final BuildProperties buildProperties;

    @GetMapping("/health")
    public String health(){
        return "API is up and running";
    }

    @GetMapping("/version")
    public String version(){
        return buildProperties.getVersion();
    }
}
