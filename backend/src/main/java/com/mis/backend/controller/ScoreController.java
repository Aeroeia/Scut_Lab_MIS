package com.mis.backend.controller;

import com.mis.backend.entity.CourseSelection;
import com.mis.backend.result.Result;
import com.mis.backend.service.ICourseSelectionService;
import com.mis.backend.vo.ScoreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/scores")
public class ScoreController {
    @Autowired
    private ICourseSelectionService courseSelectionService;
    @GetMapping
    public Result<ScoreVO> getScore(@RequestParam String studentId,@RequestParam String courseId){
        log.info("studentId: {}, courseId: {}", studentId, courseId);
        CourseSelection courseSelection = courseSelectionService.lambdaQuery()
                .eq(CourseSelection::getCourseId, courseId)
                .eq(CourseSelection::getStudentId, studentId)
                .one();
        ScoreVO scoreVO = ScoreVO.builder()
                .scoreId(courseSelection.getId())
                .score(courseSelection.getScore())
                .build();
        return Result.success(scoreVO);
    }
    @PutMapping("/{id}")
    public Result updateScore(@PathVariable Integer id, @RequestBody Map<String, BigDecimal> score){
        log.info("id:{}",id);
        courseSelectionService.lambdaUpdate()
                .eq(CourseSelection::getId, id)
                .set(CourseSelection::getScore, score.get("score"))
                .update();
        return Result.success();
    }
}
