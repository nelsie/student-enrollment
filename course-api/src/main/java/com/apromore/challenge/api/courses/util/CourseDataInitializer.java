package com.apromore.challenge.api.courses.util;

import com.apromore.challenge.api.courses.entity.Course;
import com.apromore.challenge.api.courses.repository.CourseRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Class that is loaded on startup that will populate the initial value of the Course table.
 */
@Component
@DependsOn("courseRepository")
public class CourseDataInitializer implements ApplicationRunner {
    private final CourseRepository courseRepository;

    public CourseDataInitializer(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Populate the Course table if it's empty
        if (courseRepository.count() == 0) {
            Course course1 = Course.builder().id(1L).name("Course 1").description("Description for course 1")
                    .code("CS01").deleted(false).build();
            courseRepository.save(course1);

            Course course2 = Course.builder().id(2L).name("Course 2").description("Description for course 2")
                    .code("CS02").deleted(false).build();
            courseRepository.save(course2);

            Course course3 = Course.builder().id(3L).name("Course 3").description("Description for course 3")
                    .code("CS03").deleted(false).build();
            courseRepository.save(course3);

            Course course4 = Course.builder().id(4L).name("Course 4").description("Description for course 4")
                    .code("CS04").deleted(false).build();
            courseRepository.save(course1);
        }
    }
}
