package com.mis.backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mis.backend.entity.Course;
import com.mis.backend.entity.CourseSelection;
import com.mis.backend.entity.Student;
import com.mis.backend.service.ICourseSelectionService;
import com.mis.backend.service.ICourseService;
import com.mis.backend.service.IStudentService;
import com.mis.backend.service.StatisticService;
import com.mis.backend.vo.DashboardVO;
import com.mis.backend.vo.StatisticStudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ICourseSelectionService courseSelectionService;
    @Override
    public DashboardVO getDashboard() {
        int studentCount = (int)studentService.count();
        int courseCount = (int)courseService.count();
        List<CourseSelection> courseSelections = courseSelectionService.list();
        List<BigDecimal> scores = courseSelections.stream().map(CourseSelection::getScore).toList();
        if(CollUtil.isEmpty(scores)){
            return DashboardVO.builder()
                    .studentCount(studentCount)
                    .courseCount(courseCount)
                    .build();
        }
        BigDecimal sum = scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = sum.divide(BigDecimal.valueOf(courseSelections.size()), 1, BigDecimal.ROUND_HALF_UP);
        return DashboardVO.builder()
                .studentCount(studentCount)
                .courseCount(courseCount)
                .averageScore(avg)
                .build();
    }

    @Override
    public StatisticStudentVO getStudent(String studentId, Integer academicYear) {
        StatisticStudentVO studentVO = new StatisticStudentVO();
        Student student = studentService.lambdaQuery().eq(Student::getStudentId, studentId).one();
        studentVO.setStudentId(student.getStudentId());
        studentVO.setStudentName(student.getName());
        List<CourseSelection> courseSelections = courseSelectionService.lambdaQuery()
                .eq(CourseSelection::getStudentId, studentId)
                .eq(academicYear!=null,CourseSelection::getSelectionYear, academicYear)
                .list();
        if(CollUtil.isEmpty(courseSelections)){
            return StatisticStudentVO.builder()
                    .studentName(student.getName())
                    .studentId(studentId)
                    .averageScore(BigDecimal.ZERO)
                    .courseScores(List.of())
                    .averageScore(null)
                    .build();
        }
        Map<String, BigDecimal> scoreMap = courseSelections.stream().collect(Collectors.toMap(CourseSelection::getCourseId, CourseSelection::getScore));
        List<BigDecimal> scores = new ArrayList<>(scoreMap.values());
        BigDecimal sum = scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = sum.divide(BigDecimal.valueOf(scores.size()), 1, BigDecimal.ROUND_HALF_UP);
        studentVO.setAverageScore(avg);
        Map<String, String> courseMap = courseService.lambdaQuery().list().stream().collect(Collectors.toMap(Course::getCourseId, Course::getName));
        List<StatisticStudentVO.CourseScores> courseScores = new ArrayList<>();
        int excellent = 0;
        int good = 0;
        int average = 0;
        int poor = 0;
        for(var entry:scoreMap.entrySet()){
            String courseId = entry.getKey();
            BigDecimal score = entry.getValue();
            String courseName = courseMap.get(courseId);
            StatisticStudentVO.CourseScores build = StatisticStudentVO.CourseScores.builder()
                    .score(score)
                    .courseId(courseId)
                    .courseName(courseName)
                    .build();
            courseScores.add(build);
            BigDecimal e = BigDecimal.valueOf(90);
            BigDecimal g = BigDecimal.valueOf(80);
            BigDecimal a = BigDecimal.valueOf(70);
            if(score.compareTo(e)>=0){
                excellent++;
            }
            else if(score.compareTo(g)>=0){
                good++;
            }
            else if(score.compareTo(a)>=0){
                average++;
            }
            else{
                poor++;
            }
        }
        studentVO.setCourseScores(courseScores);
        StatisticStudentVO.ScoreDistribution scoreDistribution = StatisticStudentVO.ScoreDistribution.builder()
                .excellent(excellent)
                .good(good)
                .average(average)
                .poor(poor)
                .build();
        studentVO.setScoreDistributions(scoreDistribution);
        return studentVO;
    }
}
