package com.mis.backend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mis.backend.constant.JwtClaimsConstant;
import com.mis.backend.context.BaseContext;
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
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/info")
    public Result<UserLoginVO> getProfile(){
        log.info("currentId:{}", BaseContext.getCurrentId());
        User user = userService.lambdaQuery().eq(User::getRealId, BaseContext.getCurrentId()).one();
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .realId(user.getRealId())
                .role(user.getType())
                .roleName(user.getUsername())
                .build();
        return Result.success(userLoginVO);

    }
    @PutMapping("/{realId}")
    public Result update(@PathVariable String realId,@RequestBody Map<String,String> map){
        log.info("realId:{}",realId);
        userService.lambdaUpdate().eq(User::getRealId,realId)
                .set(User::getPassword,map.get("password"))
                .update();
        return Result.success();
    }
}
