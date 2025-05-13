package com.mis.backend.service;

import com.mis.backend.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 * @author aer
 * @since 2025-05-13
 */
public interface ICourseService extends IService<Course> {

    List<Course> getCoursesByIds(List<String> ids);
}
