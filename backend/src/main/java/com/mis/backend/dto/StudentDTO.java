package com.mis.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentDTO {
    String studentId;
    String name;
    String gender;
    Integer ageAtEnrollment;
    Integer enrollmentYear;
    @JsonProperty("class")
    String clazz;
}
