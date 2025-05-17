package com.mis.backend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.mis.backend.dto.CourseDTO;
import com.mis.backend.dto.CoursePageQueryDTO;
import com.mis.backend.entity.Course;
import com.mis.backend.result.Result;
import com.mis.backend.service.ICourseService;
import com.mis.backend.vo.CourseVO;
import com.mis.backend.vo.PageQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private ICourseService courseService;
    @GetMapping
    public Result<PageQueryVO> list(CoursePageQueryDTO coursePageQueryDTO){
        log.info("coursePageQueryDTO: {}", coursePageQueryDTO);
        PageQueryVO<CourseVO> pageQueryVO = courseService.pageQuery(coursePageQueryDTO);
        return Result.success(pageQueryVO);
    }
    @PostMapping
    public Result add(@RequestBody CourseDTO courseDTO){
        log.info("courseDTO: {}", courseDTO);
        courseService.add(courseDTO);
        return Result.success();
    }
    @GetMapping("/{courseId}")
    public Result<CourseVO> getDetails(@PathVariable String courseId,@RequestParam String teacherId){
        log.info("courseId: {}", courseId);
        CourseVO courseVO = courseService.getDetails(courseId,teacherId);
        log.info("courseVO: {}", courseVO);
        return Result.success(courseVO);
    }
    @PutMapping("/{courseId}")
    public Result edit(@RequestBody CourseDTO courseDTO){
        log.info("courseDTO: {}", courseDTO);
        courseService.edit(courseDTO);
        return Result.success();
    }
    @DeleteMapping("/{courseId}")
    public Result delete(@PathVariable String courseId){
        log.info("courseId: {}", courseId);
        courseService.delete(courseId);
        return Result.success();
    }
}
