package com.mis.backend.service.impl;

import com.mis.backend.entity.Teacher;
import com.mis.backend.mapper.TeacherMapper;
import com.mis.backend.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
