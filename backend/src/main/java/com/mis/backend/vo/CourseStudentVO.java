package com.mis.backend.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseStudentVO {
    String name;
    String gender;
    @JsonProperty("class")
    String clazz;
    BigDecimal score;
    String studentId;
}
