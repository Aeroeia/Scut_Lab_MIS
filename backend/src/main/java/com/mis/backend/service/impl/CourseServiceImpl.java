package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.CourseDTO;
import com.mis.backend.dto.CoursePageQueryDTO;
import com.mis.backend.entity.*;
import com.mis.backend.mapper.CourseMapper;
import com.mis.backend.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.vo.CourseStudentVO;
import com.mis.backend.vo.CourseVO;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.StudentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ICourseSelectionService courseSelectionService;
    @Override
    public List<Course> getCoursesByIds(List<String> ids) {
        return courseMapper.getCoursesByIds(ids);
    }

    @Override
    public PageQueryVO<CourseVO> pageQuery(CoursePageQueryDTO coursePageQueryDTO) {
        String name = coursePageQueryDTO.getCourseName();
        String courseId = coursePageQueryDTO.getCourseId();
        String teacherName = coursePageQueryDTO.getTeacherName();
        
        // 构建查询条件，先不分页
        List<String> filteredCourseIds = null;
        if (StrUtil.isNotEmpty(courseId) || StrUtil.isNotEmpty(name)) {
            // 如果指定了课程ID或课程名称，先过滤课程
            filteredCourseIds = this.lambdaQuery()
                    .eq(StrUtil.isNotEmpty(courseId), Course::getCourseId, courseId)
                    .like(StrUtil.isNotEmpty(name), Course::getName, name)
                    .list()
                    .stream()
                    .map(Course::getCourseId)
                    .collect(Collectors.toList());
            
            if (CollUtil.isEmpty(filteredCourseIds)) {
                return PageQueryVO.of();
            }
        }
        
        // 构建教师ID条件
        List<String> filteredTeacherIds = null;
        if (StrUtil.isNotEmpty(teacherName)) {
            // 如果指定了教师名称，先过滤教师
            filteredTeacherIds = teacherService.lambdaQuery()
                    .like(Teacher::getName, teacherName)
                    .list()
                    .stream()
                    .map(Teacher::getTeacherId)
                    .collect(Collectors.toList());
            
            if (CollUtil.isEmpty(filteredTeacherIds)) {
                return PageQueryVO.of();
            }
        }
        
        // 在TeacherCourse表上应用分页
        Page<TeacherCourse> tcPage = coursePageQueryDTO.toMpPage();
        
        // 构建TeacherCourse查询条件
        Page<TeacherCourse> teacherCoursePage = teacherCourseService.lambdaQuery()
                .in(filteredCourseIds != null, TeacherCourse::getCourseId, filteredCourseIds)
                .in(filteredTeacherIds != null, TeacherCourse::getTeacherId, filteredTeacherIds)
                .page(tcPage);
        
        if (CollUtil.isEmpty(teacherCoursePage.getRecords())) {
            return PageQueryVO.of();
        }
        
        // 获取分页后的课程IDs和教师IDs
        List<String> pagedCourseIds = teacherCoursePage.getRecords().stream()
                .map(TeacherCourse::getCourseId)
                .distinct()
                .collect(Collectors.toList());
        
        List<String> pagedTeacherIds = teacherCoursePage.getRecords().stream()
                .map(TeacherCourse::getTeacherId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询课程和教师信息
        List<Course> courses = this.lambdaQuery()
                .in(Course::getCourseId, pagedCourseIds)
                .list();
        
        List<Teacher> teachers = teacherService.lambdaQuery()
                .in(Teacher::getTeacherId, pagedTeacherIds)
                .list();
        
        // 创建映射
        Map<String, Course> courseMap = courses.stream()
                .collect(Collectors.toMap(Course::getCourseId, course -> course));
        
        Map<String, String> teacherMap = teachers.stream()
                .collect(Collectors.toMap(Teacher::getTeacherId, Teacher::getName));
        
        // 构建结果集
        List<CourseVO> resultList = teacherCoursePage.getRecords().stream().map(tc -> {
            String thisCourseId = tc.getCourseId();
            String thisTeacherId = tc.getTeacherId();
            
            Course course = courseMap.get(thisCourseId);
            if (course == null) {
                return null; // 跳过没有找到的课程
            }
            
            CourseVO vo = new CourseVO();
            vo.setCourseId(thisCourseId);
            vo.setName(course.getName());
            vo.setCredit(course.getCredit());
            vo.setHours(course.getHours());
            vo.setSemester(course.getSemester());
            vo.setTeacherId(thisTeacherId);
            vo.setTeacherName(teacherMap.get(thisTeacherId));
            return vo;
        }).filter(vo -> vo != null).collect(Collectors.toList());
        
        // 创建分页结果
        PageQueryVO<CourseVO> pageQueryVO = new PageQueryVO<>();
        pageQueryVO.setTotal((int)teacherCoursePage.getTotal());
        pageQueryVO.setPages((int)teacherCoursePage.getPages());
        pageQueryVO.setSize((int)teacherCoursePage.getSize());
        pageQueryVO.setCurrent((int)teacherCoursePage.getCurrent());
        pageQueryVO.setRecords(resultList);
        
        return pageQueryVO;
    }

    @Override
    @Transactional
    public void add(CourseDTO courseDTO) {
        Course course = BeanUtil.copyProperties(courseDTO, Course.class);
        course.setName(courseDTO.getCourseName());
        this.save(course);
        TeacherCourse teacherCourse = BeanUtil.copyProperties(courseDTO, TeacherCourse.class);
        teacherCourseService.save(teacherCourse);
    }

    @Override
    public CourseVO getDetails(String courseId) {
        Course course = lambdaQuery().eq(Course::getCourseId, courseId).one();
        String teacherId = teacherCourseService.lambdaQuery().eq(TeacherCourse::getCourseId,courseId).one().getTeacherId();
        String teacherName = teacherService.lambdaQuery().eq(Teacher::getTeacherId,teacherId).one().getName();
        CourseVO courseVO = BeanUtil.copyProperties(course, CourseVO.class);
        courseVO.setTeacherName(teacherName);
        courseVO.setTeacherId(teacherId);
        List<CourseSelection> courseSelections = courseSelectionService.lambdaQuery()
                .eq(CourseSelection::getCourseId, courseId).list();
        Map<String, BigDecimal> map = courseSelections.stream().collect(Collectors.toMap(CourseSelection::getStudentId, CourseSelection::getScore));
        List<String> studentIds = courseSelections.stream().map(CourseSelection::getStudentId).toList();
        List<Student> students = studentService.lambdaQuery().in(Student::getStudentId, studentIds).list();
        List<CourseStudentVO> courseStudentVOS = BeanUtil.copyToList(students, CourseStudentVO.class);
        for(CourseStudentVO courseStudentVO : courseStudentVOS) {
            BigDecimal score = map.get(courseStudentVO.getStudentId());
            courseStudentVO.setScore(score);
        }
        courseVO.setStudents(courseStudentVOS);
        return courseVO;
    }
}