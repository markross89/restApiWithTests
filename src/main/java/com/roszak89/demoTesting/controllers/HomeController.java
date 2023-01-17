package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.models.Student;
import com.roszak89.demoTesting.repositories.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
public class HomeController {

    private final StudentRepository studentRepository;

    public HomeController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }



    @GetMapping("/students")
    public List<Student> students() {
        return studentRepository.findAll();
    }

    @GetMapping("/student/{id}")
    public Student getStudent(@PathVariable long id) {

        return studentRepository.findAll().stream().filter(student -> student.getId().equals(id)).findFirst().orElseThrow(()-> new IllegalArgumentException("Student not found!"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/students")
    public void addStudent(@Valid @RequestBody Student student) {
        studentRepository.save(student);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update_student/{id}")
    public void updateStudent(@Valid @RequestBody Student student, @PathVariable long id) {
        student.setId(studentRepository.findAll().stream().filter(s -> s.getId().equals(id)).findFirst().orElseThrow(()-> new IllegalArgumentException("Student not found!")).getId());
        studentRepository.save(student);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete_student/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentRepository.delete(studentRepository.findAll().stream().filter(student -> student.getId().equals(id)).findFirst().orElseThrow(()-> new IllegalArgumentException("Student not found!")));
    }

}
