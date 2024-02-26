package com.apromore.challenge.student.controller;

import com.apromore.challenge.student.client.CourseApiClientService;
import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.entity.StudentCourses;
import com.apromore.challenge.student.model.Course;
import com.apromore.challenge.student.service.StudentCoursesService;
import com.apromore.challenge.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student/courses")
@Tag(name = "StudentCourses", description = "Operations related to Student course enrollment")
public class StudentCoursesController {
    private static final Logger logger = LoggerFactory.getLogger(StudentCoursesController.class);
    private final StudentCoursesService studentCoursesService;
    private final StudentService studentService;
    private final CourseApiClientService courseApiClientService;

    @Operation(summary = "Retrieve the list of courses that a specific student is currently enrolled in.", description = "Retrieve the list of courses that a specific student is currently enrolled in.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getAllEnrolledCourses(@PathVariable Long studentId) {
        logger.info("Fetching enrolled courses for student with ID {}", studentId);
        Optional<Student> student = studentService.getByIdWithCourses(studentId);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get().getStudentCourses());
        } else {
            logger.warn("Student with ID {} not found", studentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }

    @Operation(summary = "Create a new enrollment record to student", description = "Enroll a new course to the student. The course having the input must be available in the course-api application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course enrollment added successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found or Course not found or not active"),
            @ApiResponse(responseCode = "409", description = "Student is already enrolled in the course"),
            @ApiResponse(responseCode = "5xx", description = "Internal error encountered when invoking course-api endpoint"),
    })
    @PostMapping("/{studentId}/{courseCode}")
    public ResponseEntity<?> enrollNewCourse(@PathVariable Long studentId, @PathVariable String courseCode) {
        logger.info("Enrolling new course for student with ID {}", studentId);
        Optional<Student> student = studentService.getById(studentId);
        if (student.isEmpty()) {
            return handleStudentNotFound(studentId);
        }

        Student studentEntity = student.get();
        if (studentCoursesService.isEnrolled(studentEntity, courseCode)) {
            return handleCourseAlreadyEnrolled(studentId, courseCode);
        }

        Course course = courseApiClientService.getCourseFromApiByCode(courseCode);
        if (ObjectUtils.isEmpty(course.getCode())) {
            return handleCourseNotFound(courseCode);
        }
        if (course.isDeleted()) {
            return handleCourseDeleted(courseCode);
        }
        StudentCourses studentCourses = createStudentCourses(studentEntity, course);
        return ResponseEntity.ok(studentCoursesService.saveEnrolledCourse(studentCourses));
    }

    private ResponseEntity<?> handleStudentNotFound(Long studentId) {
        logger.warn("Student with ID {} not found", studentId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
    }

    private ResponseEntity<?> handleCourseAlreadyEnrolled(Long studentId, String courseCode) {
        logger.warn("Course {} already enrolled for student with ID {}", courseCode, studentId);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Course already enrolled for student");
    }

    private ResponseEntity<?> handleCourseNotFound(String courseCode) {
        logger.warn("Course with code {} not found", courseCode);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course code not found");
    }

    private ResponseEntity<?> handleCourseDeleted(String courseCode) {
        logger.warn("Course with code {} has been deleted", courseCode);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course has been deleted");
    }

    private ResponseEntity<?> handleCourseEnrollmentNotFound(Long studentId, String courseCode) {
        logger.warn("Course enrollment for course with {} code and student with ID {} not found", courseCode, studentId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course enrollment not found");
    }

    private StudentCourses createStudentCourses(Student studentEntity, Course course) {
        return StudentCourses.builder()
                .student(studentEntity)
                .courseCode(course.getCode())
                .courseDescription(course.getDescription())
                .courseName(course.getName())
                .build();
    }

    @Operation(summary = "Delete an enrollment record", description = "Remove the course in the list of enrolled courses for the student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found or Course enrollment"),
    })
    @DeleteMapping("/{studentId}/{courseCode}")
    public ResponseEntity<?> removeEnrolledCourse(@PathVariable Long studentId, @PathVariable String courseCode) {
        logger.info("Removing enrolled course {} for student with ID {}", courseCode, studentId);
        Optional<Student> student = studentService.getById(studentId);
        if (student.isEmpty()) {
            return handleStudentNotFound(studentId);
        }

        Student studentEntity = student.get();
        StudentCourses enrolledCourse = studentCoursesService.getEnrolledCourse(studentEntity, courseCode);
        if (enrolledCourse == null) {
            return handleCourseNotFound(courseCode);
        }
        studentCoursesService.deleteEnrolledCourse(enrolledCourse);
        logger.info("Enrolled course {} removed successfully for student with ID {}", courseCode, studentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update enrollment record of the student", description = "Update the details about the enrollment of the student to the course. Since the details that can be updated are related to the course details, the updated will be retrieved from the course-api application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course enrollment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found or Course not found"),
            @ApiResponse(responseCode = "5xx", description = "Internal error encountered when invoking course-api endpoint"),
    })
    @PutMapping("/{studentId}/{courseCode}")
    public ResponseEntity<?> updateCourseEnrollment(@PathVariable Long studentId, @PathVariable String courseCode) {
        logger.info("Updating course enrollment for course with {} code and student with ID {}", courseCode, studentId);
        Optional<Student> student = studentService.getById(studentId);
        if (student.isEmpty()) {
            return handleStudentNotFound(studentId);
        }
        Student studentEntity = student.get();
        StudentCourses studentCourses = studentCoursesService.getEnrolledCourse(studentEntity, courseCode);
        if (studentCourses == null) {
            return handleCourseEnrollmentNotFound(studentId, courseCode);
        }
        Course course = courseApiClientService.getCourseFromApiByCode(courseCode);
        if (ObjectUtils.isEmpty(course.getCode())) {
            return handleCourseNotFound(courseCode);
        }
        updateStudentCourses(studentCourses,course);
        return ResponseEntity.ok(studentCoursesService.saveEnrolledCourse(studentCourses));
    }

    private void updateStudentCourses(StudentCourses studentCourses, Course course) {
        studentCourses.setCourseDescription(course.getDescription());
        studentCourses.setCourseName(course.getName());
    }
}
