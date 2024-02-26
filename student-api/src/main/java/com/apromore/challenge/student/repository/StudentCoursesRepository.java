package com.apromore.challenge.student.repository;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCoursesRepository extends JpaRepository<StudentCourses, Long> {
    List<StudentCourses> findStudentCoursesByStudent(Student student);

    StudentCourses findStudentCoursesByStudentAndCourseCode(Student student, String courseCode);
}
