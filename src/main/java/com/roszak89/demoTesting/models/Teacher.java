package com.roszak89.demoTesting.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Teacher {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id", nullable = false)
    private Long id;
    private long pay;
    private String name;





}
