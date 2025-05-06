package com.mis.backend.service.impl;

import com.mis.backend.entity.User;
import com.mis.backend.mapper.UserMapper;
import com.mis.backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
