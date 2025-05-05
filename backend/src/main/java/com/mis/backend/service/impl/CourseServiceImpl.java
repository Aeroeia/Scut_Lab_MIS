package com.mis.backend.service.impl;

import com.mis.backend.entity.Course;
import com.mis.backend.mapper.CourseMapper;
import com.mis.backend.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-05
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}
