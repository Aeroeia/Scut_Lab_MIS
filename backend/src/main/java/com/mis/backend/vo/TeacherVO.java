package com.mis.backend.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherVO implements Serializable {
    //教师编号
    String teacherId;
    //教师姓名
    String name;
    List<CourseVO> courses;
}
