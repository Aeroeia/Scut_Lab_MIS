package com.mis.backend.service;

import com.mis.backend.dto.UserLoginDTO;
import com.mis.backend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
public interface IUserService extends IService<User> {

    User login(UserLoginDTO userLoginDTO);
}
