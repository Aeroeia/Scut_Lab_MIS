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
    public Result<CourseVO> getDetails(@PathVariable String courseId){
        log.info("courseId: {}", courseId);
        CourseVO courseVO = courseService.getDetails(courseId);
        return Result.success(courseVO);
    }
}
