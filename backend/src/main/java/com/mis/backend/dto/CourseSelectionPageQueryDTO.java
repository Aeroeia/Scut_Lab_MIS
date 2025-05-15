package com.mis.backend.dto;

import lombok.Data;

@Data
public class CourseSelectionPageQueryDTO extends PageQueryDTO{
    String studentId;
    String studentName;
    String courseId;
    String courseName;
    Integer selectionYear;
}
