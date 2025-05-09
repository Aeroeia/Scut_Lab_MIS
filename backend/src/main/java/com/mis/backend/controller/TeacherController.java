package com.mis.backend.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.TeacherPageQueryDTO;
import com.mis.backend.entity.Teacher;
import com.mis.backend.result.Result;
import com.mis.backend.service.ITeacherService;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.TeacherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;
    //获取教师列表
    @GetMapping
    public Result<PageQueryVO> list(TeacherPageQueryDTO teacherPageQueryDTO){
        log.info("teacherPageQueryDTO: {}", teacherPageQueryDTO);
        Page<Teacher> mpPage = teacherPageQueryDTO.toMpPage();
        //获取教师列表
        String name = teacherPageQueryDTO.getName();
        Page<Teacher> page = teacherService.lambdaQuery().like(StrUtil.isNotEmpty(name),Teacher::getName,name).page(mpPage);
        //类型转换
        PageQueryVO<TeacherVO> teacherVOPageQueryVO = PageQueryVO.of(page, item -> BeanUtil.copyProperties(item, TeacherVO.class));
        return Result.success(teacherVOPageQueryVO);
    }
}
