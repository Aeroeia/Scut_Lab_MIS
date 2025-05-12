package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.StudentDTO;
import com.mis.backend.dto.StudentPageQueryDTO;
import com.mis.backend.entity.CourseSelection;
import com.mis.backend.entity.Student;
import com.mis.backend.entity.User;
import com.mis.backend.enums.UserRole;
import com.mis.backend.mapper.StudentMapper;
import com.mis.backend.service.ICourseSelectionService;
import com.mis.backend.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.service.IUserService;
import com.mis.backend.vo.CourseSelectionVO;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.StudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    private final StudentMapper studentMapper;
    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }
    @Autowired
    private ICourseSelectionService courseSelectionService;
    @Autowired
    private IUserService userService;
    @Override
    public PageQueryVO<StudentVO> pageQuery(StudentPageQueryDTO studentPageQueryDTO) {
        //设置分页
        Page<Student> mpPage = studentPageQueryDTO.toMpPage();
        String name = studentPageQueryDTO.getName();
        String clazz = studentPageQueryDTO.getClazz();
        String studentId = studentPageQueryDTO.getStudentId();
        //查询
        Page<Student> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(name), Student::getName, name)
                .eq(StrUtil.isNotEmpty(clazz),
                        Student::getClazz, clazz)
                .eq(StrUtil.isNotEmpty(studentId), Student::getStudentId, studentId)
                .page(mpPage);
        List<Student> records = page.getRecords();
        //类型转换
        return PageQueryVO.of(mpPage,student -> {
            return BeanUtil.copyProperties(student, StudentVO.class);
        });

    }

    @Override
    public StudentVO getDetails(String studentId,Integer year) {
        Student one = lambdaQuery().eq(Student::getStudentId, studentId).one();
        StudentVO studentVO = BeanUtil.copyProperties(one, StudentVO.class);
        List<CourseSelectionVO> courses = courseSelectionService.listCourseSelectionById(studentId,year);
        studentVO.setCourses(courses);
        return studentVO;
    }

    @Override
    public void updateByStuId(String studentId, StudentDTO studentDTO) {
        Student student = BeanUtil.copyProperties(studentDTO, Student.class);
        lambdaUpdate().eq(Student::getStudentId, studentId).update(student);
    }

    @Override
    public void add(StudentDTO studentDTO) {
        Student student = BeanUtil.copyProperties(studentDTO, Student.class);
        this.save(student);
        User build = User.builder()
                .username(student.getName())
                .type(UserRole.Student)
                .realId(student.getStudentId())
                .build();
        userService.save(build);
    }
}
