package com.apromore.challenge.student.util;

import com.apromore.challenge.student.entity.Student;
import com.apromore.challenge.student.repository.StudentRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Class that is loaded on startup that will populate the initial value of the Student table.
 */
@Component
@DependsOn("studentRepository")
public class StudentDataInitializer implements ApplicationRunner {
    private final StudentRepository studentRepository;

    public StudentDataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (studentRepository.count() == 0) {
            studentRepository.save(Student.builder().name("Student 001").age(20).id(1L).build());
            studentRepository.save(Student.builder().name("Student 002").age(30).id(2L).build());
            studentRepository.save(Student.builder().name("Student 003").age(23).id(3L).build());
            studentRepository.save(Student.builder().name("Student 004").age(25).id(4L).build());
        }
    }
}
