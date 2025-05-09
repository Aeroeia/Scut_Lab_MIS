package com.mis.backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
}
