package com.mis.backend.controller;

import com.mis.backend.dto.CoursePageQueryDTO;
import com.mis.backend.result.Result;
import com.mis.backend.service.ICourseService;
import com.mis.backend.vo.CourseVO;
import com.mis.backend.vo.PageQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
