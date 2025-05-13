package com.mis.backend.service.impl;

import com.mis.backend.entity.Course;
import com.mis.backend.mapper.CourseMapper;
import com.mis.backend.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-13
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    private final CourseMapper courseMapper;
    CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }
    @Override
    public List<Course> getCoursesByIds(List<String> ids) {
        return courseMapper.getCoursesByIds(ids);
    }
}
