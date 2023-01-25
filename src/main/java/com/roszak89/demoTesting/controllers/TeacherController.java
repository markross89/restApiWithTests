package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.exceptions.NotFoundException;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherRepository teacherRepository;


    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    @GetMapping("{id}")
    public Teacher getTeacher(@PathVariable long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found!", id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Teacher createTeacher(@Valid @RequestBody Teacher teacher) {
        teacherRepository.save(teacher);
        return teacher;
    }


    @PutMapping("{id}")
    public Teacher updateTeacher(@Valid @RequestBody Teacher newTeacher, @PathVariable long id) {
        newTeacher.setId(teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found!", id)).getId());
        teacherRepository.save(newTeacher);
        return newTeacher;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteTeacher(@PathVariable long id) {
        teacherRepository.delete(teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found!", id)));
    }
}
