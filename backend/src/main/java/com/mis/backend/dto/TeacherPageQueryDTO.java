package com.mis.backend.dto;

import com.mis.backend.vo.PageQueryVO;
import lombok.Data;

@Data
public class TeacherPageQueryDTO extends PageQueryDTO {
    //教师姓名
    private String name;
}
