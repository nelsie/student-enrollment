package com.apromore.challenge.api.courses.repository;

import com.apromore.challenge.api.courses.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testFindCourseByCode_ExistingCourse() {
        Course course = Course.builder().name("Course 1").code("COURSE-001").description("Description 1").deleted(false).id(1L).build();
        courseRepository.save(course);
        Course foundCourse = courseRepository.findCourseByCode("COURSE-001");
        assertEquals(course.getCode(), foundCourse.getCode());
        assertEquals(course.getName(), foundCourse.getName());
        assertEquals(course.getDescription(), foundCourse.getDescription());
    }

    @Test
    void testFindCourseByCode_NonExistingCourse() {
        Course foundCourse = courseRepository.findCourseByCode("NON-EXISTING-CODE");
        assertNull(foundCourse);
    }

    @Test
    void testUpdateByCode_ExistingCourse() {
        Course course = Course.builder().name("Course 2").code("COURSE-002").description("Description 2").deleted(false).id(2L).build();
        courseRepository.save(course);
        Course foundCourse = courseRepository.findCourseByCode("COURSE-002");
        assertEquals(course.getCode(), foundCourse.getCode());
        assertEquals(course.getName(), foundCourse.getName());
        assertEquals(course.getDescription(), foundCourse.getDescription());

        Course updatedCourse = Course.builder().name("New Course 2").code("COURSE-002").description("New Description 2").deleted(false).id(2L).build();
        courseRepository.save(updatedCourse);

        Course updatedFoundCourse = courseRepository.findCourseByCode("COURSE-002");
        assertEquals(updatedCourse.getCode(), updatedFoundCourse.getCode());
        assertEquals(updatedCourse.getName(), updatedFoundCourse.getName());
        assertEquals(updatedCourse.getDescription(), updatedFoundCourse.getDescription());
    }
}