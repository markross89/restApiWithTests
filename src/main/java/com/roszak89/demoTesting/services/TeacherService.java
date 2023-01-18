package com.roszak89.demoTesting.services;

import com.roszak89.demoTesting.models.Teacher;
import com.roszak89.demoTesting.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }
    public Teacher deleteTeacher(long id){
        return teacherRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Teacher not found!")).removeFromLessons();
    }
}
