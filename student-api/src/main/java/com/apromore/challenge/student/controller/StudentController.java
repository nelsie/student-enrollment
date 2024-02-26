package com.apromore.challenge.student.controller;

import com.apromore.challenge.student.entity.Student;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "Student CRUD operations", description = "CRUD Operations for Student entity")
public class StudentController {
    private final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    @Operation(summary = "Retrieve all students", description = "Retrieve the list available students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
    })
    @GetMapping
    public List<Student> getAllStudents() {
        logger.info("Fetching all students");
        return studentService.getAllStudents();
    }

    @Operation(summary = "Retrieve details of a student", description = "Retrieve details of the student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        logger.info("Fetching student  by id: {}", studentId);
        Optional<Student> student = studentService.getById(studentId);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            logger.warn("Student with ID not found: {}", studentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }

    @Operation(summary = "Create a new student record", description = "Create a new student record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student saved successfully"),
    })
    @PostMapping
    public ResponseEntity<?> addNewStudent(@RequestBody Student student) {
        logger.info("Creating new student: {}", student);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createNewStudent(student));
    }

    @Operation(summary = "Update student record", description = "Update details about the student. Only NAME and AGE are updated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
    })
    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId, @RequestBody Student student) {
        logger.info("Updating student with id: {}", studentId);
        Student updatedStudent = studentService.updateStudent(studentId, student);
        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        }
        logger.warn("Student not found with id: {}", studentId);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a student record", description = "Delete a student by setting deleted flag to true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
    })
    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        logger.info("Deleting student with code: {}", studentId);
        Student deletedStudent = studentService.deleteStudent(studentId);
        if (deletedStudent != null) {
            return ResponseEntity.ok(deletedStudent);
        }
        logger.warn("Student not found with id: {}", studentId);
        return ResponseEntity.notFound().build();
    }
}
