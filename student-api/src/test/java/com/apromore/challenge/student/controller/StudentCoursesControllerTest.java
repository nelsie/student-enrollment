package com.apromore.challenge.student.controller;

import com.apromore.challenge.student.client.CourseApiClientService;
import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import com.apromore.challenge.student.model.Course;
import com.apromore.challenge.student.service.StudentCoursesService;
import com.apromore.challenge.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentCoursesControllerTest {

    @Mock
    private StudentCoursesService studentCoursesService;

    @Mock
    private StudentService studentService;

    @Mock
    private CourseApiClientService courseApiClientService;

    @InjectMocks
    private StudentCoursesController studentCoursesController;

    @Test
    void testGetAllEnrolledCourses_Success() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getByIdWithCourses(1L)).thenReturn(Optional.of(student));
        ResponseEntity<?> responseEntity = studentCoursesController.getAllEnrolledCourses(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(student.getStudentCourses(), responseEntity.getBody());
    }

    @Test
    void testGetAllEnrolledCourses_StudentNotFound() {
        when(studentService.getByIdWithCourses(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = studentCoursesController.getAllEnrolledCourses(1L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Student not found", responseEntity.getBody());
    }

    @Test
    void testEnrollNewCourse_Success() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getById(1L)).thenReturn(Optional.of(student));
        when(studentCoursesService.isEnrolled(any(), anyString())).thenReturn(false);
        Course course = new Course();
        course.setCode("CSE101");
        when(courseApiClientService.getCourseFromApiByCode("CSE101")).thenReturn(course);

        StudentCourses expectedNewCourse = StudentCourses.builder().student(student)
                .courseCode(course.getCode())
                .build();
        when(studentCoursesService.saveEnrolledCourse(any(StudentCourses.class))).thenReturn(expectedNewCourse);

        ResponseEntity<?> responseEntity = studentCoursesController.enrollNewCourse(1L, "CSE101");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("CSE101", ((StudentCourses) responseEntity.getBody()).getCourseCode());
    }

    @Test
    void testEnrollNewCourse_StudentNotFound() {
        when(studentService.getById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = studentCoursesController.enrollNewCourse(1L, "CSE101");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Student not found", responseEntity.getBody());
    }

    @Test
    void testEnrollNewCourse_AlreadyEnrolled() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getById(1L)).thenReturn(Optional.of(student));
        when(studentCoursesService.isEnrolled(any(), anyString())).thenReturn(true);
        ResponseEntity<?> responseEntity = studentCoursesController.enrollNewCourse(1L, "CSE101");
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Course already enrolled for student", responseEntity.getBody());
    }

    @Test
    void testEnrollNewCourse_CourseNotFound() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getById(1L)).thenReturn(Optional.of(student));
        when(studentCoursesService.isEnrolled(any(), anyString())).thenReturn(false);
        when(courseApiClientService.getCourseFromApiByCode("CSE101")).thenReturn(new Course());
        ResponseEntity<?> responseEntity = studentCoursesController.enrollNewCourse(1L, "CSE101");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Course code not found", responseEntity.getBody());
    }

    @Test
    void testEnrollNewCourse_CourseDeleted() {
        Student student = new Student();
        student.setId(1L);
        Course deletedCoursed = Course.builder().code("CSE101").deleted(true).build();
        when(studentService.getById(1L)).thenReturn(Optional.of(student));
        when(studentCoursesService.isEnrolled(any(), anyString())).thenReturn(false);
        when(courseApiClientService.getCourseFromApiByCode("CSE101")).thenReturn(deletedCoursed);
        ResponseEntity<?> responseEntity = studentCoursesController.enrollNewCourse(1L, "CSE101");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Course has been deleted", responseEntity.getBody());
    }


    @Test
    void testRemoveEnrolledCourse_Success() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getById(1L)).thenReturn(Optional.of(student));
        when(studentCoursesService.getEnrolledCourse(any(), anyString())).thenReturn(new StudentCourses());
        ResponseEntity<?> responseEntity = studentCoursesController.removeEnrolledCourse(1L, "CSE101");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testRemoveEnrolledCourse_StudentNotFound() {
        when(studentService.getById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = studentCoursesController.removeEnrolledCourse(1L, "CSE101");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Student not found", responseEntity.getBody());
    }

    @Test
    void testRemoveEnrolledCourse_EnrolledCourseNotFound() {
        Student student = new Student();
        student.setId(1L);
        when(studentService.getById(1L)).thenReturn(Optional.of(student));
        when(studentCoursesService.getEnrolledCourse(any(), anyString())).thenReturn(null);
        ResponseEntity<?> responseEntity = studentCoursesController.removeEnrolledCourse(1L, "CSE101");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Course code not found", responseEntity.getBody());
    }

    @Test
    public void testUpdateCourseEnrollment_Success() {
        Long studentId = 1L;
        String courseCode = "COURSE123";
        Student student = new Student();
        student.setId(studentId);
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));
        StudentCourses studentCourses = new StudentCourses();
        when(studentCoursesService.getEnrolledCourse(student, courseCode)).thenReturn(studentCourses);
        Course course = Course.builder().name("Updated name").code(courseCode).description("New description").deleted(false).build();
        when(courseApiClientService.getCourseFromApiByCode(courseCode)).thenReturn(course);
        ResponseEntity<?> responseEntity = studentCoursesController.updateCourseEnrollment(studentId, courseCode);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(studentCoursesService, times(1)).saveEnrolledCourse(studentCourses);
    }

    @Test
    public void testUpdateCourseEnrollment_StudentNotFound() {
        Long studentId = 1L;
        String courseCode = "COURSE123";
        when(studentService.getById(studentId)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = studentCoursesController.updateCourseEnrollment(studentId, courseCode);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(studentCoursesService, never()).saveEnrolledCourse(any());
    }

    @Test
    public void testUpdateCourseEnrollment_EnrolledCourseNotFound() {
        Long studentId = 1L;
        String courseCode = "COURSE123";
        Student student = new Student();
        student.setId(studentId);
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));
        when(studentCoursesService.getEnrolledCourse(student, courseCode)).thenReturn(null);
        ResponseEntity<?> responseEntity = studentCoursesController.updateCourseEnrollment(studentId, courseCode);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(studentCoursesService, never()).saveEnrolledCourse(any());
    }

    @Test
    public void testUpdateCourseEnrollment_CourseNotFound() {
        Long studentId = 1L;
        String courseCode = "COURSE123";
        Student student = Student.builder().id(studentId).build();
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));
        StudentCourses studentCourses = new StudentCourses();
        when(studentCoursesService.getEnrolledCourse(student, courseCode)).thenReturn(studentCourses);
        Course course = new Course();
        when(courseApiClientService.getCourseFromApiByCode(courseCode)).thenReturn(course);
        ResponseEntity<?> responseEntity = studentCoursesController.updateCourseEnrollment(studentId, courseCode);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(studentCoursesService, never()).saveEnrolledCourse(any());
    }
}
