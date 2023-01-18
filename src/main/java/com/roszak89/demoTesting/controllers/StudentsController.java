package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.models.Student;
import com.roszak89.demoTesting.repositories.StudentRepository;
import com.roszak89.demoTesting.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
public class StudentsController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentsController(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }



    @GetMapping("/students")
    public List<Student> students() {
        return studentRepository.findAll();
    }

    @GetMapping("/student/{id}")
    public Student getStudent(@PathVariable long id) {
        return studentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Student not found!"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/students")
    public void addStudent(@Valid @RequestBody Student student) {
        studentRepository.save(student);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/student/{id}")
    public void updateStudent(@Valid @RequestBody Student student, @PathVariable long id) {
        student.setId(studentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Student not found!")).getId());
        studentRepository.save(student);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/student/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentRepository.delete(studentService.deleteStudent(id));
    }

}
