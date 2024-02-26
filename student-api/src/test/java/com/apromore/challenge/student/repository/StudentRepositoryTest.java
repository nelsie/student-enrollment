package com.apromore.challenge.student.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        // Insert test data into the in-memory database
        Student student = Student.builder().name("Test User 1").age(22).id(1L).build();
        // Set student properties
        StudentCourses studentCourse1 = StudentCourses.builder().courseDescription("DESC 1").courseCode("TST1").courseName("TEST 1").build();
        // Set student course properties
        studentCourse1.setStudent(student);
        List<StudentCourses> studentCourses = new ArrayList<>();
        studentCourses.add(studentCourse1);
        student.setStudentCourses(studentCourses);
        // Save the test data to the database
        studentRepository.save(student);
    }

    @Test
    void testFindByIdWithCourses_Success() {
        Optional<Student> optionalStudent = studentRepository.findByIdWithCourses(1L);
        assertTrue(optionalStudent.isPresent());
        // Assert that the student's courses are fetched eagerly
        assertEquals(1, optionalStudent.get().getStudentCourses().size());
    }

    @Test
    void testFindByIdWithCourses_StudentNotFound() {
        Optional<Student> optionalStudent = studentRepository.findByIdWithCourses(999L);
        assertFalse(optionalStudent.isPresent());
    }
}
