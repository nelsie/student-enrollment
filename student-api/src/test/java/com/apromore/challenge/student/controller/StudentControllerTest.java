package com.apromore.challenge.student.controller;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student student1() {
        return Student.builder().id(1L).name("John").age(25).build();
    }

    private Student student2() {
        return Student.builder().id(2L).name("Jane").age(30).build();
    }

    @Test
    public void testGetAllStudents() {
        List<Student> students = Arrays.asList(student1(), student2());
        when(studentService.getAllStudents()).thenReturn(students);
        List<Student> result = studentController.getAllStudents();
        assertEquals(students, result);
    }

    @Test
    public void testGetStudentById() {
        Long studentId = 1L;
        Student student = student1();
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));
        ResponseEntity<?> result = studentController.getStudentById(studentId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(student, result.getBody());
    }

    @Test
    public void testGetStudentById_NotFound() {
        Long studentId = 1L;
        when(studentService.getById(studentId)).thenReturn(Optional.empty());
        ResponseEntity<?> result = studentController.getStudentById(studentId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testAddNewStudent() {
        Student student = student1();
        when(studentService.createNewStudent(student)).thenReturn(student);
        ResponseEntity<?> result = studentController.addNewStudent(student);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(student, result.getBody());
    }


    @Test
    public void testUpdateStudent() {
        Long studentId = 1L;
        Student updatedStudent = student1();
        when(studentService.updateStudent(eq(studentId), any(Student.class))).thenReturn(updatedStudent);
        ResponseEntity<?> result = studentController.updateStudent(studentId, updatedStudent);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedStudent, result.getBody());
    }

    @Test
    public void testUpdateStudent_NotFound() {
        Long studentId = 1L;
        Student updatedStudent = student1();
        when(studentService.updateStudent(eq(studentId), any(Student.class))).thenReturn(null);
        ResponseEntity<?> result = studentController.updateStudent(studentId, updatedStudent);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testDeleteStudent() {
        Long studentId = 1L;
        Student deletedStudent = student1();
        when(studentService.deleteStudent(studentId)).thenReturn(deletedStudent);
        ResponseEntity<?> result = studentController.deleteStudent(studentId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(deletedStudent, result.getBody());
    }

    @Test
    public void testDeleteStudent_NotFound() {
        Long studentId = 1L;
        when(studentService.deleteStudent(studentId)).thenReturn(null);
        ResponseEntity<?> result = studentController.deleteStudent(studentId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
