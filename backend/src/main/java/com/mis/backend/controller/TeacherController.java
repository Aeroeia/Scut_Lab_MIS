package com.mis.backend.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mis.backend.dto.TeacherDTO;
import com.mis.backend.dto.TeacherPageQueryDTO;
import com.mis.backend.entity.Teacher;
import com.mis.backend.result.Result;
import com.mis.backend.service.ITeacherService;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.TeacherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        String title = teacherPageQueryDTO.getTitle();
        String teacherId = teacherPageQueryDTO.getTeacherId();
        Page<Teacher> page = teacherService.lambdaQuery()
                .like(StrUtil.isNotEmpty(name),Teacher::getName,name)
                .eq(StrUtil.isNotEmpty(teacherId),Teacher::getTeacherId,teacherId)
                .eq(StrUtil.isNotEmpty(title),Teacher::getTitle,title)
                .page(mpPage);
        //类型转换
        PageQueryVO<TeacherVO> teacherVOPageQueryVO = PageQueryVO.of(page, item -> BeanUtil.copyProperties(item, TeacherVO.class));
        return Result.success(teacherVOPageQueryVO);
    }
    //添加教师
    @PostMapping
    public Result add(@RequestBody TeacherDTO teacherDTO){
        log.info("teacherDTO: {}", teacherDTO);
        teacherService.add(teacherDTO);
        return Result.success();
    }
    //获取教师详情
    @GetMapping("/{teacherId}")
    public Result<TeacherVO> getDetails(@PathVariable String teacherId){
        log.info("teacherId: {}", teacherId);
        TeacherVO teacherVO = teacherService.getDetails(teacherId);
        log.info("teacherVO: {}", teacherVO.getCourses());
        return Result.success(teacherVO);
    }
    //修改教师信息
    @PutMapping("/{teacherId}")
    public Result edit(@RequestBody TeacherDTO teacherDTO){
        log.info("teacherDTO: {}", teacherDTO);
        teacherService.edit(teacherDTO);
        return Result.success();
    }
    //删除教师
    @DeleteMapping("/{teacherId}")
    public Result delete(@PathVariable String teacherId){
        log.info("teacherId: {}", teacherId);
        teacherService.delete(teacherId);
        return Result.success();
    }

}
