package com.mis.backend.dto;

import lombok.Data;

@Data
public class CoursePageQueryDTO extends PageQueryDTO {
    String courseId;
    String courseName;
    String teacherName;
}
