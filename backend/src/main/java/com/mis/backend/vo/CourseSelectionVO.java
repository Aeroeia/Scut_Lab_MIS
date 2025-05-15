package com.mis.backend.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseSelectionVO implements Serializable {
    Integer id;
    String courseId;
    String courseName;
    String teacherId;
    String teacherName;
    BigDecimal credit;
    Integer selectionYear;
    BigDecimal score;
    String studentId;
    String studentName;
}
