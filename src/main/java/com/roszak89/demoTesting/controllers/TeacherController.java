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

    @GetMapping(value="{id}")
    public Teacher getTeacher(@PathVariable long id) {
        return teacherRepository.findById(id).orElseThrow(()-> new NotFoundException("Teacher not found!", id));
    }


    @PostMapping
    public void createTeacher(@Valid @RequestBody Teacher teacher){
        teacherRepository.save(teacher);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value="{id}")
    public void updateTeacher(@Valid @RequestBody Teacher teacher, @PathVariable long id){
        teacher.setId(teacherRepository.findById(id).orElseThrow(() -> new NotFoundException("Teacher not found!",id)).getId());
        teacherRepository.save(teacher);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value="{id}")
    public void deleteTeacher(@PathVariable long id){
        teacherRepository.delete(teacherRepository.findById(id).orElseThrow(()->new NotFoundException("Teacher not found!", id)));
    }
}
