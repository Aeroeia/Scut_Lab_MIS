package com.mis.backend.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mis.backend.entity.CourseSelection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentVO {
    private String studentId; //学号
    private String name; //姓名
    private String gender; //性别
    private Integer ageAtEnrollment; //入学年龄
    private Integer enrollmentYear; //入学年份
    @JsonProperty("class")
    private String clazz; //班级
    private List<CourseSelectionVO> courses; //选课信息

}
