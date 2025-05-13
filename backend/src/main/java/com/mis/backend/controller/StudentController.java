package com.mis.backend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mis.backend.dto.StudentDTO;
import com.mis.backend.dto.StudentPageQueryDTO;
import com.mis.backend.entity.Student;
import com.mis.backend.result.Result;
import com.mis.backend.service.ICourseSelectionService;
import com.mis.backend.service.IStudentService;
import com.mis.backend.service.IUserService;
import com.mis.backend.vo.PageQueryVO;
import com.mis.backend.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private IStudentService studentService;

    //获取学生列表
    @GetMapping
    public Result<PageQueryVO<StudentVO>> list(@RequestParam(value = "class",required = false) String clazz,@ModelAttribute StudentPageQueryDTO studentPageQueryDTO){
        studentPageQueryDTO.setClazz(clazz);
        log.info("UserPageQueryDTO: {}", studentPageQueryDTO);
        PageQueryVO<StudentVO> studentVOPageQueryVO = studentService.pageQuery(studentPageQueryDTO);
        return Result.success(studentVOPageQueryVO);
    }
    //添加学生
    @PostMapping
    public Result post(@RequestBody StudentDTO studentDTO){
        log.info("Student: {}", studentDTO);
        studentService.add(studentDTO);

        return Result.success(studentDTO);
    }
    //获取学生详情
    @GetMapping("/{studentId}")
    public Result<StudentVO> getDetails(@PathVariable String studentId,Integer year){
        log.info("StudentId: {}", studentId);
        StudentVO studentVO = studentService.getDetails(studentId,year);
        log.info("StudentVO: {}", studentVO);
        return Result.success(studentVO);
    }
    //修改学生信息
    @PutMapping("/{studentId}")
    public Result update(@PathVariable String studentId,@RequestBody StudentDTO studentDTO){
        log.info("StudentId: {}", studentId);
        log.info("StudentDTO: {}", studentDTO);
        studentService.updateByStuId(studentId,studentDTO);
        return Result.success();
    }
    //删除学生
    @DeleteMapping("/{studentId}")
    public Result delete(@PathVariable String studentId){
        log.info("StudentId: {}", studentId);
        studentService.delete(studentId);
        return Result.success();
    }
}
