package com.mis.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseVO implements Serializable {
    //课程编号
    String courseId;
    //课程名称
    String courseName;
}
