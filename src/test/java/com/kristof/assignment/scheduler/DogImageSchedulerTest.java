package com.kristof.assignment.scheduler;

import com.kristof.assignment.db.entity.DogEntity;
import com.kristof.assignment.db.repository.DogRepository;
import com.kristof.assignment.model.DogCeoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DogImageSchedulerTest {

	@Test
	void updateMissingDogImage_whenDogHasNoImage_fetchesAndSaves() {
		DogRepository dogRepository = mock(DogRepository.class);
		RestTemplate restTemplate = mock(RestTemplate.class);

		DogImageScheduler scheduler = new DogImageScheduler(dogRepository, restTemplate);
		ReflectionTestUtils.setField(scheduler, "dogImageUrl", "https://example.invalid/random");

		DogEntity dog = new DogEntity();
		dog.setId(1L);
		dog.setImage("");

		when(dogRepository.findFirstByImageIsNullOrImageEquals("")).thenReturn(Optional.of(dog));

		DogCeoResponse apiResponse = new DogCeoResponse();
		apiResponse.setStatus("success");
		apiResponse.setMessage("https://images.example.invalid/dog.jpg");

		when(restTemplate.getForObject(anyString(), eq(DogCeoResponse.class))).thenReturn(apiResponse);

		scheduler.updateMissingDogImage();

		verify(dogRepository).save(argThat(saved -> "https://images.example.invalid/dog.jpg".equals(saved.getImage())));
	}

	@Test
	void updateMissingDogImage_whenNoDogMissingImage_doesNothing() {
		DogRepository dogRepository = mock(DogRepository.class);
		RestTemplate restTemplate = mock(RestTemplate.class);

		DogImageScheduler scheduler = new DogImageScheduler(dogRepository, restTemplate);

		when(dogRepository.findFirstByImageIsNullOrImageEquals("")).thenReturn(Optional.empty());

		scheduler.updateMissingDogImage();

		verify(restTemplate, never()).getForObject(anyString(), any());
		verify(dogRepository, never()).save(any());
	}
}