package com.mis.backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mis.backend.dto.UserLoginDTO;
import com.mis.backend.entity.User;
import com.mis.backend.mapper.UserMapper;
import com.mis.backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        List<User> list = lambdaQuery().eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .list();
        if(CollUtil.isEmpty(list)){
            throw new RuntimeException("用户名或密码错误");
        }
        return list.get(0);
    }

}
