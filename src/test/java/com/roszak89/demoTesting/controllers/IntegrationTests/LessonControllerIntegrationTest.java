package com.roszak89.demoTesting.controllers.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.models.Lesson;
import com.roszak89.demoTesting.repositories.LessonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser("spring")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LessonControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    ObjectMapper mapper;

    Lesson lesson1 = Lesson.builder().name("math").build();
    Lesson lesson2 = Lesson.builder().name("geography").build();
    Lesson newLesson = Lesson.builder().name("history").build();

    @BeforeEach
    void setUp() {
        List<Lesson> lessons = new ArrayList<>(Arrays.asList(lesson1, lesson2));
        lessonRepository.saveAll(lessons);
    }

    @AfterEach
    void tearDown() {
        lessonRepository.deleteAll();
    }

    @Test
    void getLessons() throws Exception {
        mockMvc.perform(get("/lesson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(lesson1.getName())))
                .andDo(print());
    }

    @Test
    void getLesson_success() throws Exception {
        mockMvc.perform(get("/lesson/{id}", lesson2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(lesson2.getName())))
                .andDo(print());
    }

    @Test
    void getLesson_noId() throws Exception {
        mockMvc.perform(get("/lesson/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Lesson not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void createLesson() throws Exception {
        mockMvc.perform(post("/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newLesson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newLesson.getName())))
                .andDo(print());
    }

    @Test
    void updateLesson_success() throws Exception {
        mockMvc.perform(put("/lesson/{id}", lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newLesson.getName())))
                .andDo(print());
    }

    @Test
    void updateLesson_noId() throws Exception {
        mockMvc.perform(put("/lesson/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newLesson)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Lesson not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void deleteLesson_success() throws Exception {
        mockMvc.perform(delete("/lesson/{id}", lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteLesson_noId() throws Exception {
        mockMvc.perform(delete("/lesson/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Lesson not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
}