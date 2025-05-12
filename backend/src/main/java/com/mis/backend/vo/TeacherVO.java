package com.mis.backend.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherVO implements Serializable {
    private Integer id;

    /**
     * 教师编号
     */
    private String teacherId;

    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;

    /**
     * 职称
     */
    private String title;

    /**
     * 所属院系
     */
    private String department;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 入职日期
     */
    private String hireDate;

    /**
     * 手机号
     */
    private String phone;
    List<CourseVO> courses;
}
