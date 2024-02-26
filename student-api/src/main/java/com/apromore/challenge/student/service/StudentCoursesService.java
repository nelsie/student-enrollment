package com.apromore.challenge.student.service;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import com.apromore.challenge.student.repository.StudentCoursesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentCoursesService {
    private final StudentCoursesRepository studentCoursesRepository;

    public StudentCourses saveEnrolledCourse(StudentCourses studentCourses) {
        return studentCoursesRepository.save(studentCourses);
    }

    public void deleteEnrolledCourse(StudentCourses studentCourses) {
        studentCoursesRepository.delete(studentCourses);
    }

    public boolean isEnrolled(Student student, String courseCode) {
        return getEnrolledCourse(student, courseCode) != null;
    }

    public StudentCourses getEnrolledCourse(Student student, String courseCode) {
        return studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, courseCode);
    }
}
