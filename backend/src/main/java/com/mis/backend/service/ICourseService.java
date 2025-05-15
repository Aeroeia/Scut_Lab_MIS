package com.mis.backend.service;

import com.mis.backend.dto.CourseDTO;
import com.mis.backend.dto.CoursePageQueryDTO;
import com.mis.backend.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mis.backend.vo.CourseVO;
import com.mis.backend.vo.PageQueryVO;

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

    PageQueryVO<CourseVO> pageQuery(CoursePageQueryDTO coursePageQueryDTO);

    void add(CourseDTO courseDTO);

    CourseVO getDetails(String courseId,String teacherId);

    void edit(CourseDTO courseDTO);

    void delete(String courseId);
}
