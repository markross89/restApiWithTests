package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.models.Lesson;
import com.roszak89.demoTesting.repositories.LessonRepository;
import com.roszak89.demoTesting.services.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LessonController {

    private final LessonRepository lessonRepository;
    private final LessonService lessonService;

    public LessonController(LessonRepository lessonRepository, LessonService lessonService) {
        this.lessonRepository = lessonRepository;
        this.lessonService = lessonService;
    }

    @GetMapping("/lessons")
    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/lessons")
    public void createLesson(@Valid @RequestBody Lesson lesson) {
        lessonRepository.save(lesson);
    }


    @GetMapping("/lesson/{id}")
    public Lesson getStudent(@PathVariable long id){
        return lessonRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Lesson not found!"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/lesson/{id}")
    public void updateLesson(@Valid @RequestBody Lesson lesson, @PathVariable long id){
        lesson.setId(lessonRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Lesson not found!")).getId());
        lessonRepository.save(lesson);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/lesson/{id}")
    public void deleteLesson(@PathVariable long id){
        lessonRepository.delete(lessonService.deleteLesson(id));
    }
}
