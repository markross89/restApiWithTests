package com.roszak89.demoTesting.controllers.UnitTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.controllers.TeacherController;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@WithMockUser("spring")
class TeacherControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TeacherRepository teacherRepository;

    Teacher teacher1 = Teacher.builder().id(1L).pay(2100).name("marek").build();
    Teacher teacher2 = Teacher.builder().id(2L).pay(2200).name("bogdan").build();
    Teacher teacher3 = Teacher.builder().id(3L).pay(2300).name("kamil").build();
    Teacher newTeacher = Teacher.builder().id(2L).pay(2900).name("baba").build();

    @Test
    void getTeachers() throws Exception {
        List<Teacher> teachers = new ArrayList<>(Arrays.asList(teacher1, teacher2, teacher3));
        Mockito.when(teacherRepository.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is(teacher3.getName())))
                .andDo(print());
    }

    @Test
    void getTeacher_success() throws Exception {
        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.ofNullable(teacher1));

        mockMvc.perform(get("/teacher/" + teacher1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(teacher1.getName())))
                .andDo(print());
    }

    @Test
    void getTeacher_noId() throws Exception {
        Mockito.when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("Teacher not found! id: 1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void createTeacher() throws Exception {
        Mockito.when(teacherRepository.save(teacher2)).thenReturn(teacher2);

        mockMvc.perform(post("/teacher")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(teacher2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(teacher2.getName())))
                .andDo(print());
    }

    @Test
    void updateTeacher_success() throws Exception {
        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.ofNullable(teacher1));
        Mockito.when(teacherRepository.save(newTeacher)).thenReturn(newTeacher);

        mockMvc.perform(put("/teacher/{id}", teacher1.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newTeacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newTeacher.getName())))
                .andDo(print());
    }

    @Test
    void updateTeacher_noId() throws Exception {
        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/teacher/{id}", teacher1.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newTeacher)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("Teacher not found! id: " + teacher1.getId(), Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void deleteTeacher_success() throws Exception {
        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.ofNullable(teacher1));

        mockMvc.perform(delete("/teacher/{id}", teacher1.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteTeacher_noTeacher() throws Exception {
        Mockito.when(teacherRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/teacher/-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("Teacher not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
}