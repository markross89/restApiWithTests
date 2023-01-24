package com.roszak89.demoTesting.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        teacherRepository.deleteAll();
    }

    @Disabled
    @Test
    void getTeachers() throws Exception {

    }

    @Disabled
    @Test
    void getTeacher() {
    }

    @WithMockUser("spring")
    @Test
    void createTeacher() throws Exception {

        // given - precondition or setup
        Teacher teacher = Teacher.builder()
                .name("marek roszak")
                .pay(2400)
                .build();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(teacher.getName())));
    }

    @Disabled
    @Test
    void updateTeacher() {
    }

    @Disabled
    @Test
    void deleteTeacher() {
    }
}