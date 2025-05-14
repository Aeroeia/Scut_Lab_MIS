package com.mis.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDTO {
    String courseId;
    String courseName;
    String teacherId;
    String teacherName;
    BigDecimal credit;
    Integer hours;
    Integer semester;
    String description;
}
