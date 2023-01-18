package com.roszak89.demoTesting.services;

import com.roszak89.demoTesting.models.Lesson;
import com.roszak89.demoTesting.repositories.LessonRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson deleteLesson(long id){
        return lessonRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Lesson not found!")).removeFromStudents().removeTeacher();
    }
}
