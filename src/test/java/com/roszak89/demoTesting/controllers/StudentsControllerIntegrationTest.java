package com.roszak89.demoTesting.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.models.Student;
import com.roszak89.demoTesting.repositories.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser("spring")
class StudentsControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ObjectMapper mapper;

    Student student1 = Student.builder().name("marek").surname("roszak").email("aaaaaaa").build();
    Student student2 = Student.builder().name("kamil").surname("paprocki").email("bbbbb").build();
    Student newStudent = Student.builder().name("brajan").surname("bobski").email("ccccc").build();

    @BeforeEach
    void setup() {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2));
        studentRepository.saveAll(students);
    }

    @AfterEach
    void drop() {
        studentRepository.deleteAll();
    }

    @Test
    void students() throws Exception {
        mockMvc.perform(get("/student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(student1.getName())));
    }

    @Test
    void getStudent_success() throws Exception {
        mockMvc.perform(get("/student/{id}", student2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(student2.getName())));
    }

    @Test
    void getStudent_noId() throws Exception {
        mockMvc.perform(get("/student/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Student not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void addStudent() throws Exception {
        ResultActions response = mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newStudent)));

        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newStudent.getName())));
    }

    @Test
    void updateStudent_success() throws Exception {
        mockMvc.perform(put("/student/{id}", student1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newStudent.getName())));
    }

    @Test
    void updateStudent_noId() throws Exception {
        mockMvc.perform(put("/student/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newStudent)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("Student not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void deleteStudent_success() throws Exception {
        mockMvc.perform(delete("/student/{id}", student1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudent_noId() throws Exception {
        mockMvc.perform(delete("/student/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Student not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}