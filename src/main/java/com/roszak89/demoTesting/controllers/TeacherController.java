package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.exceptions.NotFoundException;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "{id}")
    public Teacher getTeacher(@PathVariable long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found!", id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Teacher createTeacher(@Valid @RequestBody Teacher teacher) {
        teacherRepository.save(teacher);
        return teacher;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{id}")
    public Teacher updateTeacher(@Valid @RequestBody Teacher newTeacher, @PathVariable long id) {

        return teacherRepository.findById(id)
                .map(teacher -> {
                    teacher.setName(newTeacher.getName());
                    teacher.setPay(newTeacher.getPay());
                    return teacherRepository.save(teacher);

                })
                .orElseThrow(() -> new NotFoundException("teacher not fount", id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    public Teacher updateTeacher(@Valid @RequestBody Teacher newTeacher) {
        return teacherRepository.findById(newTeacher.getId()).map(teacher -> teacherRepository.save(newTeacher)).orElseThrow(() -> new NotFoundException("teacher not fount", newTeacher.getId()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public void deleteTeacher(@PathVariable long id) {
        teacherRepository.delete(teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found!", id)));
    }
}
