package com.roszak89.demoTesting.repositories;

import com.roszak89.demoTesting.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


}
