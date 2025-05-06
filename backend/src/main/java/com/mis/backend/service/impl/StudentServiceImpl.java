package com.mis.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.StudentPageQueryDTO;
import com.mis.backend.entity.Student;
import com.mis.backend.mapper.StudentMapper;
import com.mis.backend.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.StudentVO;
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

    @Override
    public PageQueryVO<StudentVO> pageQuery(StudentPageQueryDTO studentPageQueryDTO) {
        //设置分页
        Page<Student> mpPage = studentPageQueryDTO.toMpPage();
        String name = studentPageQueryDTO.getName();
        String clazz = studentPageQueryDTO.getClazz();
        //查询
        Page<Student> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(name), Student::getName, name)
                .like(StrUtil.isNotEmpty(clazz), Student::getClazz, clazz)
                .page(mpPage);
        List<Student> records = page.getRecords();
        //类型转换
        return PageQueryVO.of(mpPage,student -> {
            return BeanUtil.copyProperties(student, StudentVO.class);
        });

    }
}
