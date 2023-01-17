package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherController {

    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/teachers")
    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/teachers")
    public void createTeacher(@Valid @RequestBody Teacher teacher){
        teacherRepository.save(teacher);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/teacher/{id}")
    public Teacher getTeacher(@PathVariable long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Teacher not found!"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update_teacher/{id}")
    public void updateTeacher(@Valid @RequestBody Teacher teacher, long id){
        teacher.setId(teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Teacher not found!")).getId());
        teacherRepository.save(teacher);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("delete_teacher")
    public void deleteTeacher(@PathVariable long id){
        teacherRepository.deleteById(teacherRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Teacher not found!")).getId());
    }
}
