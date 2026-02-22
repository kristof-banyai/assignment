package com.kristof.assignment.controller;

import com.kristof.assignment.exception.DogNotFoundException;
import com.kristof.assignment.model.DogResponse;
import com.kristof.assignment.model.Gender;
import com.kristof.assignment.service.DogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DogController.class)
class DogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    DogService dogService;

    @Test
    void getAllDogs_returns200AndList() throws Exception {
        when(dogService.getAllDogs()).thenReturn(List.of(
                DogResponse.builder()
                        .name("Rex")
                        .breed("Beagle")
                        .gender(Gender.MALE)
                        .image("https://example.invalid/rex.jpg")
                        .build()
        ));

        mockMvc.perform(get("/api/v1/dogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Rex"));

        verify(dogService).getAllDogs();
        verifyNoMoreInteractions(dogService);
    }

    @Test
    void getDogById_returns200AndDog() throws Exception {
        when(dogService.getDogById(1L)).thenReturn(
                DogResponse.builder()
                        .name("Milo")
                        .breed("Labrador")
                        .gender(Gender.MALE)
                        .image(null)
                        .build()
        );

        mockMvc.perform(get("/api/v1/dogs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Milo"))
                .andExpect(jsonPath("$.breed").value("Labrador"));

        verify(dogService).getDogById(1L);
        verifyNoMoreInteractions(dogService);
    }

    @Test
    void getDogById_whenNotFound_returns404WithBody() throws Exception {
        when(dogService.getDogById(999L)).thenThrow(new DogNotFoundException(999L));

        mockMvc.perform(get("/api/v1/dogs/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Dog with id: 999 not found"))
                .andExpect(jsonPath("$.status").value("404"));

        verify(dogService).getDogById(999L);
        verifyNoMoreInteractions(dogService);
    }

    @Test
    void getDogById_whenIdIsNotANumber_returns400WithBody() throws Exception {
        mockMvc.perform(get("/api/v1/dogs/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Parameter id should be of type Long"));

        verifyNoInteractions(dogService);
    }

    @Test
    void createDog_returns201AndResponse() throws Exception {
        when(dogService.createDog(any())).thenReturn(
                DogResponse.builder()
                        .name("Luna")
                        .breed("Husky")
                        .gender(Gender.FEMALE)
                        .image("https://example.invalid/luna.jpg")
                        .build()
        );

        String requestJson = """
                {
                  "name": "Luna",
                  "breed": "Husky",
                  "gender": "FEMALE",
                  "image": "https://example.invalid/luna.jpg"
                }
                """;

        mockMvc.perform(post("/api/v1/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Luna"));

        verify(dogService).createDog(any());
        verifyNoMoreInteractions(dogService);
    }

    @Test
    void createDog_whenMissingRequiredFields_returns400() throws Exception {
        String invalidJson = """
                {
                  "breed": "Husky",
                  "gender": "FEMALE"
                }
                """;

        mockMvc.perform(post("/api/v1/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(dogService);
    }

    @Test
    void deleteDogById_returns200AndMessage() throws Exception {
        when(dogService.deleteDogById(5L)).thenReturn("deleted");

        mockMvc.perform(delete("/api/v1/dogs/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));

        verify(dogService).deleteDogById(5L);
        verifyNoMoreInteractions(dogService);
    }
}