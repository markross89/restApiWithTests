package com.roszak89.demoTesting.controllers;

import com.roszak89.demoTesting.exceptions.NotFoundException;
import com.roszak89.demoTesting.models.Lesson;
import com.roszak89.demoTesting.repositories.LessonRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/lesson")
public class LessonController {

    private final LessonRepository lessonRepository;


    public LessonController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping
    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }

    @GetMapping("{id}")
    public Lesson getLesson(@PathVariable long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new NotFoundException("Lesson not found!", id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Lesson createLesson(@Valid @RequestBody Lesson lesson) {
        lessonRepository.save(lesson);
        return lesson;
    }


    @PutMapping("{id}")
    public Lesson updateLesson(@Valid @RequestBody Lesson lesson, @PathVariable long id) {
        lesson.setId(lessonRepository.findById(id).orElseThrow(() -> new NotFoundException("Lesson not found!", id)).getId());
        lessonRepository.save(lesson);
        return lesson;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteLesson(@PathVariable long id) {
        lessonRepository.delete(lessonRepository.findById(id).orElseThrow(() -> new NotFoundException("Lesson not found!", id)));
    }
}
