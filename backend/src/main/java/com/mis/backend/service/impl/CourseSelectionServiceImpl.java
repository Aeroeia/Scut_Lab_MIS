package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.mis.backend.entity.Course;
import com.mis.backend.entity.CourseSelection;
import com.mis.backend.entity.Teacher;
import com.mis.backend.mapper.CourseSelectionMapper;
import com.mis.backend.service.ICourseSelectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.service.ICourseService;
import com.mis.backend.service.ITeacherService;
import com.mis.backend.vo.CourseSelectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public List<Course> getCoursesByIds(List<String> ids) {
        return courseService.lambdaQuery().in(Course::getCourseId, ids).list();
    }

    @Override
    public List<Teacher> getTeachersByIds(List<String> ids) {
        return teacherService.lambdaQuery().in(Teacher::getTeacherId, ids).list();
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
}
