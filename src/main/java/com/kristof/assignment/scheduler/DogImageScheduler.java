package com.kristof.assignment.scheduler;

import com.kristof.assignment.db.entity.DogEntity;
import com.kristof.assignment.db.repository.DogRepository;
import com.kristof.assignment.model.DogCeoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DogImageScheduler {

    private final DogRepository dogRepository;
    private final RestTemplate restTemplate;

    private static final String DOG_IMAGE_URL = "https://dog.ceo/api/breeds/image/random";

    @Scheduled(fixedDelay = 3600000)
    public void updateMissingDogImage() {
        log.info("Starting scheduled task of fetching image for dog with missing image...");

        Optional<DogEntity> optionalDog = dogRepository.findFirstByImageIsNullOrImageEquals("");

        if (optionalDog.isPresent()) {
            DogEntity dogEntity = optionalDog.get();
            log.info("Found dog with missing image, fetching new image...");

            try {
                DogCeoResponse response = restTemplate.getForObject(DOG_IMAGE_URL, DogCeoResponse.class);
                if (response != null && "success".equals(response.getStatus())) {
                    String imageUrl = response.getMessage();
                    dogEntity.setImage(imageUrl);
                    dogRepository.save(dogEntity);
                    log.info("Successfully updated image for dog with ID: {} with URL: {}", dogEntity.getId(),
                            imageUrl);
                } else {
                    log.warn("Dog API returned non-success response for dog id {}: {}", dogEntity.getId(), response);
                }
            } catch (Exception e) {
                log.error("Failed to fetch image for dog with ID: {}", dogEntity.getId(), e);
            }
        } else {
            log.info("No dog with missing image found.");
        }
    }
}
