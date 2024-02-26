package com.apromore.challenge.api.courses.service;

import com.apromore.challenge.api.courses.entity.Course;
import com.apromore.challenge.api.courses.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
    public Course updateCourse(String code, Course course) {
        Course courseForUpdate = courseRepository.findCourseByCode(code);
        if (courseForUpdate != null) {
            courseForUpdate.setName(course.getName());
            courseForUpdate.setDescription(course.getDescription());
            return courseRepository.save(courseForUpdate);
        }
        return null;
    }

    public Course deleteCourse(String code) {
        Course courseForUpdate = courseRepository.findCourseByCode(code);
        if (courseForUpdate != null) {
            courseForUpdate.setDeleted(TRUE);
            return courseRepository.save(courseForUpdate);
        }
        return null;
    }

    public Course getCourseByCode(String code) {
        return courseRepository.findCourseByCode(code);
    }
}
