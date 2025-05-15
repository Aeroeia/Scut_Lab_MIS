package com.mis.backend.service;

import com.mis.backend.dto.CourseSelectionDTO;
import com.mis.backend.dto.CourseSelectionPageQueryDTO;
import com.mis.backend.entity.Course;
import com.mis.backend.entity.CourseSelection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mis.backend.entity.Teacher;
import com.mis.backend.vo.CourseSelectionVO;
import com.mis.backend.vo.PageQueryVO;

import java.util.List;

/**
 * <p>
 * 选课表 服务类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
public interface ICourseSelectionService extends IService<CourseSelection> {
    List<Course> getCoursesByIds(List<String> ids);
    List<Teacher> getTeachersByIds(List<String> ids);
    List<CourseSelectionVO> listCourseSelectionById(String studentId,Integer year);

    PageQueryVO<CourseSelectionVO> pageQuery(CourseSelectionPageQueryDTO courseSelectionPageQueryDTO);

    void add(CourseSelectionDTO courseSelectionDTO);
}
