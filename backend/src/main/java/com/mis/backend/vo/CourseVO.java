package com.mis.backend.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseVO implements Serializable {
    //课程编号
    String courseId;
    //课程名称
    @JsonProperty("courseName")
    String name;
    BigDecimal credit;
    Integer hours;
    Integer semester;
    String teacherId;
    String teacherName;
    List<CourseStudentVO> students;
}
