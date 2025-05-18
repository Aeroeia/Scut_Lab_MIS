package com.mis.backend.controller;

import com.mis.backend.result.Result;
import com.mis.backend.service.StatisticService;
import com.mis.backend.vo.DashboardVO;
import com.mis.backend.vo.StatisticStudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
