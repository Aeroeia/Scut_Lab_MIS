package com.mis.backend.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserRole {
    Admin(1,"管理员"),
    Teacher(2,"老师"),
    Student(3,"学生");


    @EnumValue
    @JsonValue
    private final int val;
    private final String des;


    UserRole(int val,String des){
        this.val = val;
        this.des = des;
    }
}
