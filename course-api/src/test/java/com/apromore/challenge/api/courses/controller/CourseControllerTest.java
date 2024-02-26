package com.apromore.challenge.api.courses.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.apromore.challenge.api.courses.entity.Course;
import com.apromore.challenge.api.courses.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCourses() {
        // Mocking the service method
        Course course1 = Course.builder().name("Course 1").code("CST01").description("CS1").deleted(false).id(1L).build();
        Course course2 = Course.builder().name("Course 2").code("CST02").description("CS2").deleted(false).id(2L).build();
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course1, course2));

        // Calling the controller method
        List<Course> courses = courseController.getAllCourses();

        // Verifying the results
        assertEquals(2, courses.size());
        assertEquals("CST01", courses.get(0).getCode());
        assertEquals("Course 1", courses.get(0).getName());
        assertEquals("CS1", courses.get(0).getDescription());
        assertEquals("CST02", courses.get(1).getCode());
        assertEquals("Course 2", courses.get(1).getName());
        assertEquals("CS2", courses.get(1).getDescription());

        // Verifying that service method was called
        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void testGetCourseByCode() {
        // Mocking the service method
        Course course1 = Course.builder().name("Course 1").code("CST01").description("CS1").deleted(false).id(1L).build();
        when(courseService.getCourseByCode("CST01")).thenReturn(course1);

        // Calling the controller method
        ResponseEntity<?> response = courseController.getCourseByCode("CST01");

        // Verifying the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course1, response.getBody());

        // Verifying that service method was called
        verify(courseService, times(1)).getCourseByCode("CST01");
    }

    @Test
    void testGetCourseByCode_NotFound() {
        // Mocking the service method
        when(courseService.getCourseByCode("CST01")).thenReturn(null);

        // Calling the controller method
        ResponseEntity<?> response = courseController.getCourseByCode("CST01");

        // Verifying the results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found", response.getBody());

        // Verifying that service method was called
        verify(courseService, times(1)).getCourseByCode("CST01");
    }

    @Test
    void testCreateCourse() {
        // Mocking the service method
        Course course1 = Course.builder().name("Course 1").code("CST01").description("CS1").deleted(false).id(1L).build();
        when(courseService.getCourseByCode("CST01")).thenReturn(null);
        when(courseService.createCourse(course1)).thenReturn(course1);

        // Calling the controller method
        ResponseEntity<?> response = courseController.createCourse(course1);

        // Verifying the results
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(course1, response.getBody());

        // Verifying that service method was called
        verify(courseService, times(1)).getCourseByCode("CST01");
        verify(courseService, times(1)).createCourse(course1);
    }

    @Test
    void testCreateCourse_Conflict() {
        // Mocking the service method
        Course course = Course.builder().name("Course 1").code("CST01").description("CS1").deleted(false).id(1L).build();
        when(courseService.getCourseByCode("CST01")).thenReturn(course);

        // Calling the controller method
        ResponseEntity<?> response = courseController.createCourse(course);

        // Verifying the results
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Course with this code already exists", response.getBody());

        // Verifying that service method was called
        verify(courseService, times(1)).getCourseByCode("CST01");
        verify(courseService, never()).createCourse(course);
    }

    @Test
    void testUpdateCourse() {
        // Mocking the service method
        Course updatedCourse = Course.builder().name("Course 1").code("CST01").description("CS1 new desc").deleted(false).id(1L).build();
        when(courseService.updateCourse("CST01", updatedCourse)).thenReturn(updatedCourse);

        // Calling the controller method
        ResponseEntity<Course> response = (ResponseEntity<Course>) courseController.updateCourse("CST01", updatedCourse);

        // Verifying the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCourse, response.getBody());

        // Verifying that service method was called
        verify(courseService, times(1)).updateCourse("CST01", updatedCourse);
    }

    @Test
    void testUpdateCourse_NotFound() {
        // Mocking the service method
        when(courseService.updateCourse("CST01", new Course())).thenReturn(null);

        // Calling the controller method
        ResponseEntity<Course> response = (ResponseEntity<Course>) courseController.updateCourse("CST01", new Course());

        // Verifying the results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verifying that service method was called
        verify(courseService, times(1)).updateCourse("CST01", new Course());
    }

    @Test
    void testDeleteCourse() {
        // Mocking the service method
        Course deletedCourse = Course.builder().name("Course 1").code("CST01").description("CS1 new desc").deleted(false).id(1L).build();
        when(courseService.deleteCourse("CST01")).thenReturn(deletedCourse);

        // Calling the controller method
        ResponseEntity<?> response = courseController.deleteCourse("CST01");

        // Verifying the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedCourse, response.getBody());

        // Verifying that service method was called
        verify(courseService, times(1)).deleteCourse("CST01");
    }

    @Test
    void testDeleteCourse_NotFound() {
        // Mocking the service method
        when(courseService.deleteCourse("CST01")).thenReturn(null);

        // Calling the controller method
        ResponseEntity<?> response = courseController.deleteCourse("CST01");

        // Verifying the results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verifying that service method was called
        verify(courseService, times(1)).deleteCourse("CST01");
    }
}
