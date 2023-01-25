package com.roszak89.demoTesting.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;

import org.junit.jupiter.api.*;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    Teacher teacher1 = Teacher.builder().name("marek").pay(2100).build();
    Teacher teacher2 = Teacher.builder().name("kamil").pay(2200).build();
    Teacher newTeacher = Teacher.builder().name("bogdan").pay(2300).build();

    @BeforeEach
    void setup() {
        List<Teacher> teachers = new ArrayList<>(Arrays.asList(teacher1, teacher2));
        teacherRepository.saveAll(teachers);
    }

    @AfterEach
    void finish() {
        teacherRepository.deleteAll();
    }


    @WithMockUser("spring")
    @Test
    void getTeachers() throws Exception {
        mockMvc.perform(get("/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("marek")))
                .andDo(print());
        ;
    }

    @WithMockUser("spring")
    @Test
    void getTeacher_success() throws Exception {
        mockMvc.perform(get("/teacher/{id}", teacher1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(teacher1.getName())))
                .andDo(print());
        ;
    }

    @WithMockUser("spring")
    @Test
    void getTeacher_noId() throws Exception {
        mockMvc.perform(get("/teacher/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("Teacher not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @WithMockUser("spring")
    @Test
    void createTeacher() throws Exception {
        mockMvc.perform(post("/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeacher)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newTeacher.getName())))
                .andDo(print());

//      * more readable could be version with split request and response *

//        ResultActions response = mockMvc.perform(post("/teacher")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(newTeacher)));
//
//        response.andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name", is(newTeacher.getName())))
//                .andDo(print());
    }

    @WithMockUser("spring")
    @Test
    void updateTeacher_success() throws Exception {
        mockMvc.perform(put("/teacher/{id}", teacher1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newTeacher.getName())))
                .andDo(print());
    }

    @WithMockUser("spring")
    @Test
    void updateTeacher_noId() throws Exception {
        mockMvc.perform(put("/teacher/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTeacher)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Teacher not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @WithMockUser("spring")
    @Test
    void deleteTeacher_success() throws Exception {
        mockMvc.perform(delete("/teacher/{id}", teacher1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @WithMockUser("spring")
    @Test
    void deleteTeacher_noId() throws Exception {
        mockMvc.perform(delete("/teacher/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Teacher not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
}