package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mis.backend.dto.TeacherDTO;
import com.mis.backend.entity.Course;
import com.mis.backend.entity.Teacher;
import com.mis.backend.entity.TeacherCourse;
import com.mis.backend.entity.User;
import com.mis.backend.enums.UserRole;
import com.mis.backend.mapper.TeacherMapper;
import com.mis.backend.service.ICourseService;
import com.mis.backend.service.ITeacherCourseService;
import com.mis.backend.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.service.IUserService;
import com.mis.backend.vo.CourseVO;
import com.mis.backend.vo.TeacherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 教师表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-12
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private IUserService userService;
    @Autowired
    private ITeacherCourseService teacherCourseService;
    @Autowired
    private ICourseService courseService;
    private final TeacherMapper teacherMapper;
    TeacherServiceImpl(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }
    @Override
    public void add(TeacherDTO teacherDTO) {
        Teacher teacher = BeanUtil.copyProperties(teacherDTO, Teacher.class);
        this.save(teacher);
        User user = User.builder()
                .realId(teacher.getTeacherId())
                .type(UserRole.Teacher)
                .username(teacher.getName())
                .build();
        userService.save(user);
    }

    @Override
    public TeacherVO getDetails(String teacherId) {
        TeacherVO teacherVO = new TeacherVO();
        Teacher teacher = lambdaQuery().eq(Teacher::getTeacherId, teacherId).one();
        BeanUtil.copyProperties(teacher, teacherVO);
        List<TeacherCourse> list = teacherCourseService.lambdaQuery().eq(TeacherCourse::getTeacherId, teacher.getTeacherId()).list();
        List<String> courseIds = list.stream().map(TeacherCourse::getCourseId).toList();
        List<Course> courses = courseService.lambdaQuery().in(Course::getCourseId, courseIds).list();
        teacherVO.setCourses(BeanUtil.copyToList(courses, CourseVO.class));
        return teacherVO;
    }

    @Override
    @Transactional
    public void delete(String teacherId) {
        this.lambdaUpdate().eq(Teacher::getTeacherId, teacherId).remove();
        userService.lambdaUpdate().eq(User::getRealId, teacherId).remove();
        teacherCourseService.lambdaUpdate().in(TeacherCourse::getTeacherId, teacherId).remove();
    }

    @Override
    public List<Teacher> getTeachersByIds(List<String> ids) {
        return teacherMapper.getTeachersByIds(ids);
    }

    @Override
    public void edit(TeacherDTO teacherDTO) {
        String teacherId = teacherDTO.getTeacherId();
        Teacher teacher = BeanUtil.copyProperties(teacherDTO, Teacher.class);
        lambdaUpdate().eq(Teacher::getTeacherId, teacherId).update(teacher);
    }
}
