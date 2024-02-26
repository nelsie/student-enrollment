package com.apromore.challenge.api.courses.controller;

import com.apromore.challenge.api.courses.entity.Course;
import com.apromore.challenge.api.courses.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@Tag(name = "Course CRUD operations", description = "CRUD Operations for Course entity")
public class CourseController {
    private final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    @Operation(summary = "Retrieve all course", description = "Retrieve the list available course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
    })
    @GetMapping
    public List<Course> getAllCourses() {
        logger.info("Fetching all courses");
        return courseService.getAllCourses();
    }

    @Operation(summary = "Retrieve details of a course", description = "Retrieve details of the course having the input code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{code}")
    public ResponseEntity<?> getCourseByCode(@PathVariable String code) {
        logger.info("Fetching course by code: {}", code);
        Course course = courseService.getCourseByCode(code);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            logger.warn("Course not found with code: {}", code);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }

    @Operation(summary = "Create a new course record", description = "Create a new course record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course saved successfully"),
            @ApiResponse(responseCode = "409", description = "Course having the input code already exists"),
    })
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        logger.info("Creating new course: {}", course);
        Course existingCourse = courseService.getCourseByCode(course.getCode());
        if (existingCourse != null) {
            logger.warn("Course with code {} already exists", course.getCode());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course with this code already exists");
        }
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @Operation(summary = "Update an existing course record", description = "Update a course record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course saved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/{code}")
    public ResponseEntity<?> updateCourse(@PathVariable String code, @RequestBody Course course) {
        logger.info("Updating course with code: {}", code);
        Course updatedCourse = courseService.updateCourse(code, course);
        if (updatedCourse != null) {
            logger.warn("Course not found with code: {}", code);
            return ResponseEntity.ok(updatedCourse);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete an existing course record", description = "Delete a course record by setting deleted flag to true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCourse(@PathVariable String code) {
        logger.info("Deleting course with code: {}", code);
        Course deletedCourse = courseService.deleteCourse(code);
        if (deletedCourse != null) {
            return ResponseEntity.ok(deletedCourse);
        }
        logger.warn("Course not found with code: {}", code);
        return ResponseEntity.notFound().build();
    }
}
