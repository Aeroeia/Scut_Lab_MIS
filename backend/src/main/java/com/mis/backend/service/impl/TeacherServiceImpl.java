package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mis.backend.dto.TeacherDTO;
import com.mis.backend.entity.Teacher;
import com.mis.backend.entity.User;
import com.mis.backend.enums.UserRole;
import com.mis.backend.mapper.TeacherMapper;
import com.mis.backend.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 教师表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-12
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private IUserService userService;
    @Override
    public void add(TeacherDTO teacherDTO) {
        Teacher teacher = BeanUtil.copyProperties(teacherDTO, Teacher.class);
        this.save(teacher);
        User user = User.builder()
                .realId(teacher.getTeacherId())
                .type(UserRole.Teacher)
                .username(teacher.getName())
                .build();
        userService.save(user);
    }
}
