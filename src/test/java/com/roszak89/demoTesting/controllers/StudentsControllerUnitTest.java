package com.roszak89.demoTesting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roszak89.demoTesting.models.Student;
import com.roszak89.demoTesting.repositories.StudentRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentsController.class)
@WithMockUser("spring")
class StudentsControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StudentRepository studentRepository;

    Student student1 = Student.builder().id(1L).name("marek").surname("roszak").email("aaaa").build();
    Student student2 = Student.builder().id(2L).name("brajan").surname("bobo").email("bbbb").build();
    Student newStudent = Student.builder().id(3L).name("kris").surname("hajto").email("cccc").build();

    @Test
    void students() throws Exception {
        List<Student> students = new ArrayList<>(Arrays.asList(student1, student2));

        Mockito.when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(get("/student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(student1.getName())))
                .andDo(print());
    }

    @Test
    void getStudent_success() throws Exception {
        Mockito.when(studentRepository.findById(student2.getId())).thenReturn(Optional.ofNullable(student2));

        mockMvc.perform(get("/student/{id}", student2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(student2.getName())))
                .andDo(print());
    }

    @Test
    void getStudent_noId() throws Exception {
        Mockito.when(studentRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Student not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    void addStudent() throws Exception {
        Mockito.when(studentRepository.save(newStudent)).thenReturn(newStudent);

        mockMvc.perform(post("/student")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newStudent.getName())))
                .andDo(print());
    }

    @Test
    void updateStudent_success() throws Exception {

        Mockito.when(studentRepository.findById(student2.getId())).thenReturn(Optional.ofNullable(student2));
        Mockito.when(studentRepository.save(newStudent)).thenReturn(newStudent);

        mockMvc.perform(put("/student/{id}", student2.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newStudent.getName())));
    }

    @Test
    void updateStudent_noId() throws Exception {
        Mockito.when(studentRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/student/{id}", -1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newStudent)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Student not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void deleteStudent_success() throws Exception {
        Mockito.when(studentRepository.findById(student2.getId())).thenReturn(Optional.ofNullable(student2));

        mockMvc.perform(delete("/student/{id}", student2.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteStudent_noId() throws Exception {
        Mockito.when(studentRepository.findById(-1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/student/{id}", -1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Student not found! id: -1", Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }
}