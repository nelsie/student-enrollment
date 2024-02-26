package com.apromore.challenge.student.repository;

import com.apromore.challenge.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.studentCourses WHERE s.id = :studentId")
    Optional<Student> findByIdWithCourses(@Param("studentId") Long studentId);
}
