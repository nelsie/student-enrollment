package com.apromore.challenge.student.service;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import com.apromore.challenge.student.repository.StudentCoursesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentCoursesServiceTest {

    @Mock
    private StudentCoursesRepository studentCoursesRepository;

    @InjectMocks
    private StudentCoursesService studentCoursesService;

    @Test
    public void testAddNewEnrolledCourse() {
        StudentCourses studentCourses = new StudentCourses();
        when(studentCoursesRepository.save(studentCourses)).thenReturn(studentCourses);

        StudentCourses result = studentCoursesService.saveEnrolledCourse(studentCourses);

        verify(studentCoursesRepository, times(1)).save(studentCourses);
        assertEquals(studentCourses, result);
    }

    @Test
    public void testDeleteEnrolledCourse() {
        StudentCourses studentCourses = new StudentCourses();

        studentCoursesService.deleteEnrolledCourse(studentCourses);

        verify(studentCoursesRepository, times(1)).delete(studentCourses);
    }

    @Test
    public void testIsEnrolled_WhenEnrolled() {
        Student student = new Student();
        String courseCode = "CSE101";

        when(studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, courseCode))
                .thenReturn(new StudentCourses());

        boolean result = studentCoursesService.isEnrolled(student, courseCode);

        assertTrue(result);
    }

    @Test
    public void testIsEnrolled_WhenNotEnrolled() {
        Student student = new Student();
        String courseCode = "CSE101";

        when(studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, courseCode))
                .thenReturn(null);

        boolean result = studentCoursesService.isEnrolled(student, courseCode);

        assertFalse(result);
    }

    @Test
    public void testGetEnrolledCourse_WhenExists() {
        Student student = new Student();
        String courseCode = "CSE101";
        StudentCourses studentCourses = new StudentCourses();

        when(studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, courseCode))
                .thenReturn(studentCourses);

        StudentCourses result = studentCoursesService.getEnrolledCourse(student, courseCode);

        assertEquals(studentCourses, result);
    }

    @Test
    public void testGetEnrolledCourse_WhenNotExists() {
        Student student = new Student();
        String courseCode = "CSE101";

        when(studentCoursesRepository.findStudentCoursesByStudentAndCourseCode(student, courseCode))
                .thenReturn(null);

        StudentCourses result = studentCoursesService.getEnrolledCourse(student, courseCode);

        assertNull(result);
    }
}
