package com.mis.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseSelectionDTO {
    String studentId;
    String studentName;
    String courseId;
    String courseName;
    String teacherId;
    String teacherName;
    BigDecimal credit;
    Integer selectionYear;

}
