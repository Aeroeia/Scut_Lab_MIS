package com.mis.backend.service;

import com.mis.backend.dto.StudentDTO;
import com.mis.backend.dto.StudentPageQueryDTO;
import com.mis.backend.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.StudentVO;

import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author aer
 * @since 2025-05-06
 */
public interface IStudentService extends IService<Student> {

    PageQueryVO<StudentVO> pageQuery(StudentPageQueryDTO userPageQueryDTO);

    StudentVO getDetails(String studentId,Integer year);

    void updateByStuId(String studentId, StudentDTO studentDTO);

    void add(StudentDTO studentDTO);
}
