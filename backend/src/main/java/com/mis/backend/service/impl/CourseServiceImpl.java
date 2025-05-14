package com.mis.backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.CoursePageQueryDTO;
import com.mis.backend.entity.Course;
import com.mis.backend.entity.Teacher;
import com.mis.backend.entity.TeacherCourse;
import com.mis.backend.mapper.CourseMapper;
import com.mis.backend.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.service.ITeacherCourseService;
import com.mis.backend.service.ITeacherService;
import com.mis.backend.vo.CourseVO;
import com.mis.backend.vo.PageQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private ITeacherCourseService teacherCourseService;
    @Autowired
    private ITeacherService teacherService;
    @Override
    public List<Course> getCoursesByIds(List<String> ids) {
        return courseMapper.getCoursesByIds(ids);
    }

    @Override
    public PageQueryVO<CourseVO> pageQuery(CoursePageQueryDTO coursePageQueryDTO) {
        String name = coursePageQueryDTO.getCourseName();
        String courseId = coursePageQueryDTO.getCourseId();
        String teacherName = coursePageQueryDTO.getTeacherName();
        
        // 获取所有符合条件的教师
        List<Teacher> teachers = null;
        if (StrUtil.isNotEmpty(teacherName)) {
            teachers = teacherService.lambdaQuery().like(Teacher::getName, teacherName).list();
            if (CollUtil.isEmpty(teachers)) {
                return PageQueryVO.of();
            }
        }
        
        // 构建课程查询条件
        Page<Course> mpPage = coursePageQueryDTO.toMpPage();
        Page<Course> coursePage = this.lambdaQuery()
                .like(StrUtil.isNotEmpty(name), Course::getName, name)
                .eq(StrUtil.isNotEmpty(courseId), Course::getCourseId, courseId)
                .page(mpPage);
        
        if (CollUtil.isEmpty(coursePage.getRecords())) {
            return PageQueryVO.of();
        }
        
        // 获取所有课程ID
        List<String> queryCourseIds = coursePage.getRecords().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList());
        
        // 查询课程-教师关联关系
        List<TeacherCourse> teacherCourses;
        if (teachers != null) {
            // 根据课程ID和教师ID查询
            List<String> teacherIds = teachers.stream()
                    .map(Teacher::getTeacherId)
                    .collect(Collectors.toList());
            
            teacherCourses = teacherCourseService.lambdaQuery()
                    .in(TeacherCourse::getCourseId, queryCourseIds)
                    .in(TeacherCourse::getTeacherId, teacherIds)
                    .list();
        } else {
            // 仅根据课程ID查询
            teacherCourses = teacherCourseService.lambdaQuery()
                    .in(TeacherCourse::getCourseId, queryCourseIds)
                    .list();
        }
        
        if (CollUtil.isEmpty(teacherCourses)) {
            // 如果没有关联的教师信息，只返回课程基本信息
            return PageQueryVO.of(coursePage, course -> {
                CourseVO courseVO = new CourseVO();
                courseVO.setCourseId(course.getCourseId());
                courseVO.setName(course.getName());
                courseVO.setCredit(course.getCredit());
                courseVO.setHours(course.getHours());
                courseVO.setSemester(course.getSemester());
                return courseVO;
            });
        }
        
        // 获取所有需要的教师ID
        List<String> allTeacherIds = teacherCourses.stream()
                .map(TeacherCourse::getTeacherId)
                .distinct()
                .collect(Collectors.toList());
        
        // 查询教师信息
        teachers = teacherService.lambdaQuery()
                .in(Teacher::getTeacherId, allTeacherIds)
                .list();
        
        // 创建教师ID到教师名称的映射
        Map<String, String> teacherIdToNameMap = teachers.stream()
                .collect(Collectors.toMap(Teacher::getTeacherId, Teacher::getName));
        
        // 创建课程ID到课程的映射
        Map<String, Course> courseIdToCourseMap = coursePage.getRecords().stream()
                .collect(Collectors.toMap(Course::getCourseId, course -> course));
        
        // 创建结果集
        List<CourseVO> resultList = new ArrayList<>();
        for (TeacherCourse tc : teacherCourses) {
            String thisCourseId = tc.getCourseId();
            String thisTeacherId = tc.getTeacherId();
            
            // 获取课程信息
            Course course = courseIdToCourseMap.get(thisCourseId);
            if (course == null) {
                continue; // 跳过没有找到的课程
            }
            
            // 创建VO对象
            CourseVO courseVO = new CourseVO();
            courseVO.setCourseId(thisCourseId);
            courseVO.setName(course.getName());
            courseVO.setCredit(course.getCredit());
            courseVO.setHours(course.getHours());
            courseVO.setSemester(course.getSemester());
            
            // 设置教师信息
            courseVO.setTeacherId(thisTeacherId);
            courseVO.setTeacherName(teacherIdToNameMap.get(thisTeacherId));
            
            resultList.add(courseVO);
        }
        
        // 创建自定义分页结果
        PageQueryVO<CourseVO> pageQueryVO = new PageQueryVO<>();
        pageQueryVO.setTotal(resultList.size());
        pageQueryVO.setCurrent(coursePageQueryDTO.getPage());
        pageQueryVO.setSize(coursePageQueryDTO.getSize());
        
        // 手动分页处理
        int start = (coursePageQueryDTO.getPage() - 1) * coursePageQueryDTO.getSize();
        int end = Math.min(start + coursePageQueryDTO.getSize(), resultList.size());
        if (start < resultList.size()) {
            pageQueryVO.setRecords(resultList.subList(start, end));
        } else {
            pageQueryVO.setRecords(new ArrayList<>());
        }
        
        // 计算总页数
        int pages = (int) Math.ceil((double) resultList.size() / coursePageQueryDTO.getSize());
        pageQueryVO.setPages(pages);
        
        return pageQueryVO;
    }
}