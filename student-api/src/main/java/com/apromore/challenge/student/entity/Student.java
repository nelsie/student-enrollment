package com.apromore.challenge.student.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    // set to true if deleted operation to student is performed.
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentCourses> studentCourses;
}
