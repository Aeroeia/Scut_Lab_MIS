package com.mis.backend.service.impl;

import com.mis.backend.entity.Student;
import com.mis.backend.mapper.StudentMapper;
import com.mis.backend.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
