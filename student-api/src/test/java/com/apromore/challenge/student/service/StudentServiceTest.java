package com.apromore.challenge.student.service;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetByIdWithCourses_Exists() {
        Student student = Student.builder().id(1L).name("Test User 1").age(22).build();
        when(studentRepository.findByIdWithCourses(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentService.getByIdWithCourses(1L);
        verify(studentRepository, times(1)).findByIdWithCourses(1L);
        assertTrue(result.isPresent());
        assertEquals(student, result.get());
    }

    @Test
    public void testGetByIdWithCourses_NotExists() {
        when(studentRepository.findByIdWithCourses(1L)).thenReturn(Optional.empty());
        Optional<Student> result = studentService.getByIdWithCourses(1L);
        verify(studentRepository, times(1)).findByIdWithCourses(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetById_Exists() {
        Student student = Student.builder().id(1L).name("Test User 1").age(22).build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentService.getById(1L);
        verify(studentRepository, times(1)).findById(1L);
        assertTrue(result.isPresent());
        assertEquals(student, result.get());
    }

    @Test
    public void testGetById_NotExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Student> result = studentService.getById(1L);
        verify(studentRepository, times(1)).findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllStudents() {
        // Given
        Student student1 = Student.builder().id(1L).name("Nelsie").age(30).build();
        Student student2 = Student.builder().id(2L).name("Jane").age(30).build();
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);
        List<Student> result = studentService.getAllStudents();
        assertEquals(students, result);
    }

    @Test
    public void testCreateNewStudent() {
        Student newStudent = Student.builder().name("Nelsie").age(30).build();
        Student savedStudent = Student.builder().id(1L).name("Nelsie").age(30).build();
        when(studentRepository.save(newStudent)).thenReturn(savedStudent);
        Student result = studentService.createNewStudent(newStudent);
        assertEquals(savedStudent, result);
    }

    @Test
    public void testUpdateStudent() {
        Long studentId = 1L;
        Student existingStudent = Student.builder().id(studentId).name("John").age(30).build();
        Student updatedStudent = Student.builder().id(studentId).name("John Doe").age(30).build();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);
        Student result = studentService.updateStudent(studentId, updatedStudent);
        assertEquals(updatedStudent, result);
        assertEquals("John Doe", existingStudent.getName());
        assertEquals(30, existingStudent.getAge());
    }

    @Test
    public void testDeleteStudent() {
        Long studentId = 1L;
        Student existingStudent = Student.builder().id(studentId).name("John").age(30).deleted(false).build();
        Student deletedStudent = Student.builder().id(studentId).name("John").age(30).deleted(true).build();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(deletedStudent);
        Student result = studentService.deleteStudent(studentId);
        assertEquals(deletedStudent, result);
        assertTrue(existingStudent.isDeleted());
    }
}
