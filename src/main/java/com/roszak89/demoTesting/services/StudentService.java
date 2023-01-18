package com.roszak89.demoTesting.services;

import com.roszak89.demoTesting.models.Student;
import com.roszak89.demoTesting.repositories.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public Student deleteStudent(long id){
        return studentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Student not found!")).removeFromLessons();
    }
}
