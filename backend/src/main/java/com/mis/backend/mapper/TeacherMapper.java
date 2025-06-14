package com.mis.backend.mapper;

import com.mis.backend.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 教师表 Mapper 接口
 * </p>
 *
 * @author aer
 * @since 2025-05-12
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    List<Teacher> getTeachersByIds(List<String> ids);
}
