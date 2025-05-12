package com.mis.backend.controller;

import com.mis.backend.constant.JwtClaimsConstant;
import com.mis.backend.dto.UserLoginDTO;
import com.mis.backend.entity.User;
import com.mis.backend.properties.JwtProperties;
import com.mis.backend.result.Result;
import com.mis.backend.service.IUserService;
import com.mis.backend.service.impl.UserServiceImpl;
import com.mis.backend.util.JwtUtil;
import com.mis.backend.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登陆信息:{}",userLoginDTO);
        User user = userService.login(userLoginDTO);
        //生成JWT令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.Id,user.getId());
        claims.put(JwtClaimsConstant.Type,user.getType());
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims
        );
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .realId(user.getRealId())
                .token(token)
                .role(user.getType())
                .roleName(user.getUsername())
                .build();
        return Result.success(userLoginVO);
    }

    @PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }
}
