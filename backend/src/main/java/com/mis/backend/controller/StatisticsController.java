package com.mis.backend.controller;

import com.mis.backend.result.Result;
import com.mis.backend.service.StatisticService;
import com.mis.backend.vo.DashboardVO;
import com.mis.backend.vo.StatisticClassVO;
import com.mis.backend.vo.StatisticCourseVO;
import com.mis.backend.vo.StatisticStudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticService statisticService;
    @GetMapping("/dashboard")
    public Result<DashboardVO> getDashboard(){
        DashboardVO dashboardVO = statisticService.getDashboard();
        log.info("Dashboard statistics: {}", dashboardVO);
        return Result.success(dashboardVO);
    }
    @GetMapping("/student/{studentId}")
    public Result<StatisticStudentVO> getStudent(@PathVariable String studentId,Integer year){
        log.info("Student statistics: {}", studentId);
        log.info("Academic year: {}", year);
        StatisticStudentVO studentVO = statisticService.getStudent(studentId,year);
        log.info("Student statistics: {}", studentVO);
        return Result.success(studentVO);
    }
    @GetMapping("/class/{className}")
    public Result<StatisticClassVO> getClass(@PathVariable String className, @RequestParam Integer year){
        log.info("Class statistics: {}", className);
        log.info("Academic year: {}", year);
        StatisticClassVO statisticClassVO = statisticService.getstatisticClass(className,year);
        log.info("Class statistics: {}", statisticClassVO);
        return Result.success(statisticClassVO);
    }
    @GetMapping("/course/{courseId}")
    public Result<StatisticCourseVO> getCourse(@PathVariable String courseId,@RequestParam Integer year){
        log.info("Course statistics: {}", courseId);
        log.info("Academic year: {}", year);
        StatisticCourseVO statisticCourseVO = statisticService.getCourse(courseId,year);
        return Result.success(statisticCourseVO);
    }
}
