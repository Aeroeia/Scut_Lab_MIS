package com.mis.backend.vo;

import com.mis.backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginVO implements Serializable {
    private String token;
    private UserRole role; //角色类型
    private String roleName;//用户昵称
    private String realId; //学生学号、教师编号
}
