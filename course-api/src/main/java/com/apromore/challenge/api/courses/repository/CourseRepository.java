package com.apromore.challenge.api.courses.repository;

import com.apromore.challenge.api.courses.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findCourseByCode(String code);

}
