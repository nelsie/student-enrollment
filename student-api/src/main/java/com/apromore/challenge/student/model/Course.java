package com.apromore.challenge.student.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private String name;
    private String code;
    private String description;
    private boolean deleted;
}
