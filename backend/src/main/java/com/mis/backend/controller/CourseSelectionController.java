package com.mis.backend.controller;

import com.mis.backend.dto.CourseSelectionDTO;
import com.mis.backend.dto.CourseSelectionPageQueryDTO;
import com.mis.backend.entity.CourseSelection;
import com.mis.backend.result.Result;
import com.mis.backend.service.ICourseSelectionService;
import com.mis.backend.vo.CourseSelectionVO;
import com.mis.backend.vo.PageQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/course-selections")
public class CourseSelectionController {
    @Autowired
    private ICourseSelectionService courseSelectionService;
    @GetMapping
    public Result<PageQueryVO<CourseSelectionVO>> list(CourseSelectionPageQueryDTO courseSelectionPageQueryDTO){
        log.info("CourseSelectionController list:{}", courseSelectionPageQueryDTO);
        PageQueryVO<CourseSelectionVO> result = courseSelectionService.pageQuery(courseSelectionPageQueryDTO);
        log.info("CourseSelectionController list:{}", result);
        return Result.success(result);
    }
    @PostMapping
    public Result add(@RequestBody CourseSelectionDTO courseSelectionDTO){
        log.info("CourseSelectionController add:{}", courseSelectionDTO);
        courseSelectionService.add(courseSelectionDTO);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id){
        log.info("CourseSelectionController delete:{}", id);
        courseSelectionService.lambdaUpdate().eq(CourseSelection::getId,id).remove();
        return Result.success();
    }
}
