package com.apromore.challenge.student.service;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

/**
 * Service class for {@link Student} entity.
 */
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Optional<Student> getByIdWithCourses(Long id) {
        return studentRepository.findByIdWithCourses(id);
    }

    public Optional<Student> getById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createNewStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Updates the student details having the input ID
     * @param studentId the id of the Student for update
     * @param student the Student instance having the new values of the student details
     * @return the updated Student record.
     */
    public Student updateStudent(Long studentId, Student student) {
        Optional<Student> studentForUpdate = getById(studentId);
        if (studentForUpdate.isPresent()) {
            Student updatedStudent = studentForUpdate.get();
            updatedStudent.setName(student.getName());
            updatedStudent.setAge(student.getAge());
            return studentRepository.save(updatedStudent);
        }
        return null;
    }

    /**
     * Set the deleted flag of the student to TRUE.
     * @param id the id of the Student to be deleted
     * @return the details about the Deleted student
     */
    public Student deleteStudent(Long id) {
        Optional<Student> studentForDelete = getById(id);
        if (studentForDelete.isPresent()) {
            Student tobeDeleted = studentForDelete.get();
            tobeDeleted.setDeleted(TRUE);
            return studentRepository.save(tobeDeleted);
        }
        return null;
    }
}
