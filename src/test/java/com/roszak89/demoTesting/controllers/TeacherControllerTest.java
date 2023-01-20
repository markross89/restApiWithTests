package com.roszak89.demoTesting.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TeacherRepository teacherRepository;

    Teacher teacher1 = new Teacher(1L, 2100L, "marek roszak");
    Teacher teacher2 = new Teacher(2L, 2200L, "arek roszak");
    Teacher teacher3 = new Teacher(3L, 2300L, "jarek roszak");

    @WithMockUser(value = "spring")
    @Test
    void getTeachers() throws Exception {

        List<Teacher> teachers = new ArrayList<>(Arrays.asList(teacher1, teacher2, teacher3));
        Mockito.when(teacherRepository.findAll()).thenReturn(teachers);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("jarek roszak")));
    }

    @WithMockUser(value = "spring")
    @Test
    void getTeacher() throws Exception {

        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.ofNullable(teacher1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("marek roszak")));
    }


    @Test
    @WithMockUser(value = "spring")
    void createTeacher() throws Exception {
        Teacher teacher = Teacher.builder()
                .id(22L)
                .name("marian opania")
                .pay(2500)
                .build();

        Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);

        ResultActions response = mockMvc.perform(post("/teacher", Teacher.class)


                .content(this.mapper.writeValueAsString(teacher)));

        response.andExpect(status().isCreated());

    }

    @WithMockUser(value = "spring")
    @Test
    void updateTeacher() {
    }
    @WithMockUser(value = "spring")
    @Test
    void deleteTeacher() {
    }
}