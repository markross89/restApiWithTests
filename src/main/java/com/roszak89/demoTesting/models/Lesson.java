package com.roszak89.demoTesting.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher mainTeacher;

    @ManyToMany( mappedBy = "lessons")
    private Set<Student> students;


    public Lesson removeFromStudents(){
        students.forEach(s->s.getLessons().remove(this));
        students.clear();
        return this;
    }

    public Lesson removeTeacher(){
        mainTeacher.getLessons().remove(this);
        this.setMainTeacher(null);
        return this;
    }




}
