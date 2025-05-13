package com.mis.backend.service;

import com.mis.backend.dto.TeacherDTO;
import com.mis.backend.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mis.backend.vo.TeacherVO;

import java.util.List;

/**
 * <p>
 * 教师表 服务类
 * </p>
 *
 * @author aer
 * @since 2025-05-12
 */
public interface ITeacherService extends IService<Teacher> {

    void add(TeacherDTO teacherDTO);

    TeacherVO getDetails(String teacherId);

    void delete(String teacherId);

    List<Teacher> getTeachersByIds(List<String> ids);
}
