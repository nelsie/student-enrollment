package com.apromore.challenge.student.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentCoursesRepositoryTest {

    @Autowired
    private StudentCoursesRepository studentCoursesRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        // Insert test data into the in-memory database
        Student student = Student.builder().name("Test User 1").age(22).id(1L).build();
        studentRepository.save(student);
        // Set student properties
        StudentCourses studentCourse1 = StudentCourses.builder().student(student).courseCode("TST01")
                .courseName("Test1").courseDescription("Desc 1").id(1L).build();
        // Save the test data to the database
        studentCoursesRepository.save(studentCourse1);
    }

    @Test
    void testFindStudentCoursesByStudent_Success() {
        Student student = Student.builder().id(1L).build();
        List<StudentCourses> studentCoursesList = studentCoursesRepository.findStudentCoursesByStudent(student);
        assertEquals(1, studentCoursesList.size());
    }

    @Test
    void testFindStudentCoursesByStudentAndCourseCode_Success() {
        Student student = Student.builder().id(1L).build();
        StudentCourses studentCourse = studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, "TST01");
        assertEquals("TST01", studentCourse.getCourseCode());
    }

    @Test
    void testFindStudentCoursesByStudent_NotFound() {
        Student student = Student.builder().id(2L).build();
        List<StudentCourses> studentCoursesList = studentCoursesRepository.findStudentCoursesByStudent(student);
        assertTrue(studentCoursesList.isEmpty());
    }

    @Test
    void testFindStudentCoursesByStudentAndCourseCode_NotFound() {
        Student student = Student.builder().id(1L).build();
        StudentCourses studentCourse = studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, "NO_CODE");
        assertNull(studentCourse);
    }
}
