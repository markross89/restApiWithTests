package com.roszak89.demoTesting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.nio.charset.StandardCharsets;
import java.util.*;


import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @WithMockUser("spring")
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

    @WithMockUser("spring")
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

    @WithMockUser("spring")
    @Test
    void createTeacher() throws Exception {
        Teacher teacher = Teacher.builder()
                .id(22L)
                .name("marian opania")
                .pay(2500)
                .build();

        Mockito.when(teacherRepository.save(teacher)).thenReturn(teacher);


        mockMvc.perform(post("/teacher")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(teacher.getName())));
    }

    @WithMockUser("spring")
    @Test
    void updateTeacher_success() throws Exception {
        Teacher newTeacher = Teacher.builder()
                .name("Ram")
                .pay(2900)
                .build();

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/teacher/{id}", teacher3.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTeacher)));


        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
//                .andExpect(jsonPath("$.name", is(newTeacher.getName())));

    }



    @WithMockUser("spring")
    @Test
    void deleteTeacher_success() throws Exception {

        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.ofNullable(teacher1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/teacher/" + teacher1.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser("spring")
    @Test
    void deleteTeacher_noTeacher() throws Exception {

        Mockito.when(teacherRepository.findById(teacher1.getId())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/teacher/29")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("Teacher not found! id: 29", Objects.requireNonNull(result.getResolvedException()).getMessage()));
        ;

    }
}