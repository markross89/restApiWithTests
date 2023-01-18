package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.exceptions.NotFoundException;
import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;
import com.roszak89.demoTesting.services.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;

    public TeacherController(TeacherRepository teacherRepository, TeacherService teacherService) {
        this.teacherRepository = teacherRepository;
        this.teacherService = teacherService;
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

    @GetMapping("teacher/{id}")
    public Teacher getTeacher(@PathVariable long id) {
        return teacherRepository.findById(id).orElseThrow(()-> new NotFoundException("Teacher not found!", id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/teacher/{id}")
    public void updateTeacher(@Valid @RequestBody Teacher teacher, @PathVariable long id){
        teacher.setId(teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Teacher not found!")).getId());
        teacherRepository.save(teacher);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/teacher/{id}")
    public void deleteTeacher(@PathVariable long id){
        teacherRepository.delete(teacherService.deleteTeacher(id));
    }
}
