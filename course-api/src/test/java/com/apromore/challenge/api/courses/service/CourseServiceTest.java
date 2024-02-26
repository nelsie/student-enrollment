package com.apromore.challenge.api.courses.service;

import com.apromore.challenge.api.courses.entity.Course;
import com.apromore.challenge.api.courses.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Course course1() {
        return Course.builder().name("Course 1").code("COURSE-001").description("Description 1").deleted(false).id(1L).build();
    }

    private Course course2() {
        return Course.builder().name("Course 2").code("COURSE-002").description("Description 2").deleted(false).id(2L).build();
    }

    @Test
    void testGetAllCourses() {
        // Mocking the repository method
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1(), course2()));

        // Calling the service method
        List<Course> courses = courseService.getAllCourses();

        // Verifying the results
        assertEquals(2, courses.size());
        assertEquals("COURSE-001", courses.get(0).getCode());
        assertEquals("Course 1", courses.get(0).getName());
        assertEquals("Description 1", courses.get(0).getDescription());
        assertEquals("COURSE-002", courses.get(1).getCode());
        assertEquals("Course 2", courses.get(1).getName());
        assertEquals("Description 2", courses.get(1).getDescription());

        // Verifying that repository method was called
        verify(courseRepository).findAll();
    }

    @Test
    void testGetCourseById_ExistingCourse() {
        // Mocking the repository method to return a course
        Course course = course1();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Calling the service method
        Course result = courseService.getCourseById(1L);

        // Verifying the result
        assertEquals(course, result);

        // Verifying that repository method was called
        verify(courseRepository).findById(1L);
    }

    @Test
    void testGetCourseById_NonExistingCourse() {
        // Mocking the repository method to return an empty Optional (course not found)
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Calling the service method
        Course result = courseService.getCourseById(1L);

        // Verifying the result
        assertNull(result);

        // Verifying that repository method was called
        verify(courseRepository).findById(1L);
    }

    @Test
    void testCreateCourse() {
        // Creating a new course
        Course newCourse = Course.builder().name("New Course").code("COURSE-003")
                .description("Description for New Course").deleted(false).build();
        // Mocking the repository method to return the created course
        when(courseRepository.save(newCourse)).thenReturn(newCourse);

        // Calling the service method
        Course result = courseService.createCourse(newCourse);

        // Verifying the result
        assertEquals(newCourse, result);

        // Verifying that repository method was called
        verify(courseRepository).save(newCourse);
    }

    @Test
    void testUpdateCourse_ExistingCourse() {
        // Existing course
        Course existingCourse =course1();
        when(courseRepository.findCourseByCode("COURSE-001")).thenReturn(existingCourse);

        // Updated course
        Course updatedCourse = course1();
        updatedCourse.setDescription("Course 001 new description");

        // Mocking the repository method to return the updated course
        when(courseRepository.save(existingCourse)).thenReturn(updatedCourse);

        // Calling the service method
        Course result = courseService.updateCourse("COURSE-001", updatedCourse);

        // Verifying the result
        assertEquals(updatedCourse, result);

        // Verifying that repository method was called
        verify(courseRepository).findCourseByCode("COURSE-001");
        verify(courseRepository).save(existingCourse);
    }

    @Test
    void testUpdateCourse_NonExistingCourse() {
        // Mocking the repository method to return null (course not found)
        when(courseRepository.findCourseByCode("NON-EXISTING-CODE")).thenReturn(null);

        // Calling the service method with a non-existing course
        Course updatedCourse = Course.builder().name("Non existing Course").code("NON-EXISTING-CODE")
                .description("Description for New Course").deleted(false).build();
        Course result = courseService.updateCourse("NON-EXISTING-CODE", updatedCourse);

        // Verifying the result
        assertNull(result);

        // Verifying that repository method was called
        verify(courseRepository).findCourseByCode("NON-EXISTING-CODE");
        verifyNoMoreInteractions(courseRepository); // No other interactions should occur
    }

    @Test
    void testDeleteCourse_ExistingCourse() {
        // Existing course
        Course existingCourse = course1();
        when(courseRepository.findCourseByCode("COURSE-001")).thenReturn(existingCourse);

        // Mocking the repository method to return the deleted course
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        // Calling the service method
        Course result = courseService.deleteCourse("COURSE-001");

        // Verifying the result
        assertEquals(existingCourse, result);
        assertTrue(existingCourse.isDeleted()); // Course should be marked as deleted

        // Verifying that repository method was called
        verify(courseRepository).findCourseByCode("COURSE-001");
        verify(courseRepository).save(existingCourse);
    }

    @Test
    void testDeleteCourse_NonExistingCourse() {
        // Mocking the repository method to return null (course not found)
        when(courseRepository.findCourseByCode("NON-EXISTING-CODE")).thenReturn(null);

        // Calling the service method with a non-existing course
        Course result = courseService.deleteCourse("NON-EXISTING-CODE");

        // Verifying the result
        assertNull(result);

        // Verifying that repository method was called
        verify(courseRepository).findCourseByCode("NON-EXISTING-CODE");
        verifyNoMoreInteractions(courseRepository); // No other interactions should occur
    }

    @Test
    void testGetCourseByCode_ExistingCourse() {
        // Mocking the repository method to return a course
        Course course = course1();
        when(courseRepository.findCourseByCode("COURSE-001")).thenReturn(course);

        // Calling the service method
        Course result = courseService.getCourseByCode("COURSE-001");

        // Verifying the result
        assertEquals(course, result);

        // Verifying that repository method was called
        verify(courseRepository).findCourseByCode("COURSE-001");
    }

    @Test
    void testGetCourseByCode_NonExistingCourse() {
        // Mocking the repository method to return null (course not found)
        when(courseRepository.findCourseByCode("NON-EXISTING-CODE")).thenReturn(null);

        // Calling the service method
        Course result = courseService.getCourseByCode("NON-EXISTING-CODE");

        // Verifying the result
        assertNull(result);

        // Verifying that repository method was called
        verify(courseRepository).findCourseByCode("NON-EXISTING-CODE");
    }
}
