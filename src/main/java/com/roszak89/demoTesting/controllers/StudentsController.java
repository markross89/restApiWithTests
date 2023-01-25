package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.exceptions.NotFoundException;
import com.roszak89.demoTesting.models.Student;
import com.roszak89.demoTesting.repositories.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;


import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentsController {

    private final StudentRepository studentRepository;

    public StudentsController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @GetMapping
    public List<Student> students() {
        return studentRepository.findAll();
    }

    @GetMapping("{id}")
    public Student getStudent(@PathVariable long id) {
        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student not found!", id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        studentRepository.save(student);
        return student;
    }

    @PutMapping("{id}")
    public Student updateStudent(@Valid @RequestBody Student student, @PathVariable long id) {
        student.setId(studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student not found!", id)).getId());
        studentRepository.save(student);
        return student;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable long id) {
        studentRepository.delete(studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student not found!", id)));
    }

}
