package com.roszak89.demoTesting.controllers.UnitTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.controllers.LessonController;
import com.roszak89.demoTesting.models.Lesson;
import com.roszak89.demoTesting.repositories.LessonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser("spring")
@WebMvcTest(LessonController.class)
class LessonControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    LessonRepository lessonRepository;

    Lesson lesson1 = Lesson.builder().id(1L).name("math").build();
    Lesson lesson2 = Lesson.builder().id(2L).name("geography").build();
    Lesson newLesson = Lesson.builder().id(3L).name("history").build();

    @Test
    void getLessons() throws Exception {
        List<Lesson> lessons = new ArrayList<>(Arrays.asList(lesson1, lesson2));

        Mockito.when(lessonRepository.findAll()).thenReturn(lessons);

        mockMvc.perform(get("/lesson")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(lesson1.getName())))
                .andDo(print());
    }

    @Test
    void getLesson_success() throws Exception {
        Mockito.when(lessonRepository.findById(lesson1.getId())).thenReturn(Optional.ofNullable(lesson1));

        mockMvc.perform(get("/lesson/{id}", lesson1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(lesson1.getName())))
                .andDo(print());
    }

    @Test
    void getLesson_noId() throws Exception {
        Mockito.when(lessonRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/lesson/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Lesson not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void createLesson() throws Exception {
        Mockito.when(lessonRepository.save(newLesson)).thenReturn(newLesson);

        mockMvc.perform(post("/lesson")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newLesson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newLesson.getName())))
                .andDo(print());
    }

    @Test
    void updateLesson_success() throws Exception {
        Mockito.when(lessonRepository.findById(lesson1.getId())).thenReturn(Optional.ofNullable(lesson1));
        Mockito.when(lessonRepository.save(newLesson)).thenReturn(newLesson);

        mockMvc.perform(put("/lesson/{id}", lesson1.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newLesson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newLesson.getName())))
                .andDo(print());
    }

    @Test
    void updateLesson_noId() throws Exception {
        Mockito.when(lessonRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/lesson/{id}", -1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newLesson)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Lesson not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void deleteLesson_success() throws Exception {
        Mockito.when(lessonRepository.findById(lesson1.getId())).thenReturn(Optional.ofNullable(lesson1));

        mockMvc.perform(delete("/lesson/{id}", lesson1.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteLesson_noId() throws Exception {
        Mockito.when(lessonRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/lesson/{id}", -1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Lesson not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
}