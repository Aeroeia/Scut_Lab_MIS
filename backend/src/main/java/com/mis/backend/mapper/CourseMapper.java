package com.mis.backend.mapper;

import com.mis.backend.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author aer
 * @since 2025-05-13
 */
public interface CourseMapper extends BaseMapper<Course> {
    List<Course> getCoursesByIds(List<String> ids);
}
