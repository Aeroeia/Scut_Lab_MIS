package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.CourseSelectionDTO;
import com.mis.backend.dto.CourseSelectionPageQueryDTO;
import com.mis.backend.entity.*;
import com.mis.backend.mapper.CourseSelectionMapper;
import com.mis.backend.mapper.TeacherMapper;
import com.mis.backend.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.vo.CourseSelectionVO;
import com.mis.backend.vo.PageQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 选课表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
@Service
public class CourseSelectionServiceImpl extends ServiceImpl<CourseSelectionMapper, CourseSelection> implements ICourseSelectionService {
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITeacherCourseService teacherCourseService;
    @Override
    public List<Course> getCoursesByIds(List<String> ids) {
       return courseService.getCoursesByIds(ids);
    }

    @Override
    public List<Teacher> getTeachersByIds(List<String> ids) {
        return teacherService.getTeachersByIds(ids);
    }

    @Override
    public List<CourseSelectionVO> listCourseSelectionById(String studentId,Integer year) {
        List<CourseSelection> courseSelections = lambdaQuery().eq(CourseSelection::getStudentId, studentId)
                .eq(year!=null,CourseSelection::getSelectionYear,year)
                .list();
        if(CollUtil.isEmpty(courseSelections)){
            return List.of();
        }
        List<String> courseIds = courseSelections.stream().map(CourseSelection::getCourseId).collect(Collectors.toList());
        List<String> teacherIds = courseSelections.stream().map(CourseSelection::getTeacherId).collect(Collectors.toList());
        List<Course> coursesByIds = getCoursesByIds(courseIds);
        List<Teacher> teachersByIds = getTeachersByIds(teacherIds);
        
        // 创建结果列表
        List<CourseSelectionVO> courseSelectionVOS = BeanUtil.copyToList(courseSelections, CourseSelectionVO.class);
        Map<String,Course> courseMap = coursesByIds.stream().collect(Collectors.toMap(Course::getCourseId, c -> c));
        Map<String,Teacher> teacherMap = teachersByIds.stream().collect(Collectors.toMap(Teacher::getTeacherId, c -> c));
        for(CourseSelectionVO courseSelectionVO : courseSelectionVOS){
            courseSelectionVO.setCourseName(courseMap.get(courseSelectionVO.getCourseId()).getName());
            courseSelectionVO.setTeacherName(teacherMap.get(courseSelectionVO.getTeacherId()).getName());
            courseSelectionVO.setCredit(courseMap.get(courseSelectionVO.getCourseId()).getCredit());
        }
        return courseSelectionVOS;
    }

    @Override
    public PageQueryVO<CourseSelectionVO> pageQuery(CourseSelectionPageQueryDTO courseSelectionPageQueryDTO) {
        String studentId = courseSelectionPageQueryDTO.getStudentId();
        String studentName = courseSelectionPageQueryDTO.getStudentName();
        String courseId = courseSelectionPageQueryDTO.getCourseId();
        String courseName = courseSelectionPageQueryDTO.getCourseName();
        Integer selectionYear = courseSelectionPageQueryDTO.getSelectionYear();
        Page<CourseSelection> mpPage = courseSelectionPageQueryDTO.toMpPage();
        List<Student> students = studentService.lambdaQuery().eq(StrUtil.isNotEmpty(studentId), Student::getStudentId, studentId)
                .like(StrUtil.isNotEmpty(studentName), Student::getName, studentName)
                .list();
        List<Course> courses = courseService.lambdaQuery().eq(StrUtil.isNotEmpty(courseId), Course::getCourseId, courseId)
                .like(StrUtil.isNotEmpty(courseName), Course::getName, courseName)
                .list();
        Map<String, String> studentMap = students.stream().collect(Collectors.toMap(Student::getStudentId, Student::getName));
        Map<String, String> courseMap = courses.stream().collect(Collectors.toMap(Course::getCourseId, Course::getName));
        List<String> studentIds = new ArrayList<>(studentMap.keySet());
        List<String> courseIds = new ArrayList<>(courseMap.keySet());
        Page<CourseSelection> pages = this.lambdaQuery().in(CollUtil.isNotEmpty(studentIds),CourseSelection::getStudentId, studentIds)
                .in(CollUtil.isNotEmpty(courseIds),CourseSelection::getCourseId, courseIds)
                .eq(selectionYear != null, CourseSelection::getSelectionYear, selectionYear)
                .page(mpPage);
        List<CourseSelection> courseSelections = pages.getRecords();
        List<String> teacherIds = courseSelections.stream().map(CourseSelection::getTeacherId).distinct().toList();
        TeacherMapper teacherMapper = (TeacherMapper) teacherService.getBaseMapper();
        List<Teacher> teachers = teacherMapper.getTeachersByIds(teacherIds);
        Map<String, String> map = teachers.stream().collect(Collectors.toMap(Teacher::getTeacherId, Teacher::getName));
        List<CourseSelectionVO> courseSelectionVOS = BeanUtil.copyToList(courseSelections, CourseSelectionVO.class);
        for(CourseSelectionVO courseSelectionVO: courseSelectionVOS){
            String teacherId = courseSelectionVO.getTeacherId();
            String courseIdt = courseSelectionVO.getCourseId();
            String studentIdt = courseSelectionVO.getStudentId();
            courseSelectionVO.setTeacherName(map.get(teacherId));
            courseSelectionVO.setCourseName(courseMap.get(courseIdt));
            courseSelectionVO.setStudentName(studentMap.get(studentIdt));
        }
        PageQueryVO<CourseSelectionVO> pageQueryVO = new PageQueryVO<>();
        pageQueryVO.setPages((int)mpPage.getPages());
        pageQueryVO.setTotal((int)mpPage.getTotal());
        pageQueryVO.setRecords(courseSelectionVOS);
        pageQueryVO.setSize(courseSelectionVOS.size());
        pageQueryVO.setCurrent((int)mpPage.getCurrent());
        return pageQueryVO;
    }

    @Override
    @Transactional
    public void add(CourseSelectionDTO courseSelectionDTO) {
        String courseId = courseSelectionDTO.getCourseId();
        String studentId = courseSelectionDTO.getStudentId();
        CourseSelection one = this.lambdaQuery().eq(CourseSelection::getCourseId, courseId)
                .eq(CourseSelection::getStudentId, studentId)
                .one();
        if(one!=null){
            throw new RuntimeException("该学生已选这门课");
        }
        this.save(BeanUtil.copyProperties(courseSelectionDTO, CourseSelection.class));
    }
}
