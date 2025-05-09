package com.mis.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class StudentPageQueryDTO extends PageQueryDTO {
    private String name;
    private String clazz;
    private String studentId;
}
