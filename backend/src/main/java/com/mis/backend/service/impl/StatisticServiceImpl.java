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
import com.mis.backend.vo.StatisticClassVO;
import com.mis.backend.vo.StatisticStudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
        Map<String, Course> collect = courseService.lambdaQuery().list().stream().collect(Collectors.toMap(Course::getCourseId, o -> o));
        List<StatisticStudentVO.CourseScores> courseScores = new ArrayList<>();
        int excellent = 0;
        int good = 0;
        int average = 0;
        int poor = 0;
        for(var entry:scoreMap.entrySet()){
            String courseId = entry.getKey();
            BigDecimal score = entry.getValue();
            Course course = collect.get(courseId);
            StatisticStudentVO.CourseScores build = StatisticStudentVO.CourseScores.builder()
                    .score(score)
                    .courseId(courseId)
                    .courseName(course.getName())
                    .credit(course.getCredit())
                    .semester(course.getSemester())
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

    @Override
    public StatisticClassVO getstatisticClass(String className, Integer year) {
        List<Student> students = studentService.lambdaQuery().eq(Student::getClazz, className).list();
        Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getStudentId, o -> o));
        List<CourseSelection> courseSelections = courseSelectionService.lambdaQuery()
                .in(CourseSelection::getStudentId, studentMap.keySet())
                .eq(year!=null,CourseSelection::getSelectionYear,year)
                .list();
        Map<String, List<CourseSelection>> collect = courseSelections.stream().collect(Collectors.groupingBy(CourseSelection::getStudentId));
        Map<String, BigDecimal> scoreMap = courseSelections.stream().collect(Collectors.toMap(
                CourseSelection::getStudentId,
                CourseSelection::getScore,
                BigDecimal::add
        ));
        Integer studentCount = collect.size();
        if(collect.size()==0||students.size()==0){
            return StatisticClassVO.builder()
                    .excellentRate(BigDecimal.ZERO)
                    .studentCount(students.size())
                    .highestScore(BigDecimal.ZERO)
                    .lowestScore(BigDecimal.ZERO)
                    .averageScore(BigDecimal.ZERO)
                    .studentScores(List.of())
                    .build();
        }
        Map<String,BigDecimal> avgScores= new HashMap<>();
        for(var entry:scoreMap.entrySet()){
            String studentId = entry.getKey();
            BigDecimal sum = entry.getValue();
            int total = collect.get(studentId).size();
            avgScores.put(studentId,sum.divide(BigDecimal.valueOf(total),1,BigDecimal.ROUND_HALF_UP));
        }
        List<String> studentArray = new ArrayList<>(avgScores.keySet());
        Collections.sort(studentArray,(o1,o2)->avgScores.get(o2).compareTo(avgScores.get(o1)));
        BigDecimal sum = BigDecimal.ZERO;
        for(var entry:avgScores.entrySet()){
            sum = sum.add(entry.getValue());
        }
        BigDecimal classAvg = sum.divide(BigDecimal.valueOf(avgScores.size()),1,BigDecimal.ROUND_HALF_UP);
        int index = 0;
        while(index<studentCount&&avgScores.get(studentArray.get(index)).compareTo(BigDecimal.valueOf(90))>=0){
            index++;
        }
        BigDecimal excellentRate = BigDecimal.valueOf(index).divide(BigDecimal.valueOf(studentCount),1,BigDecimal.ROUND_HALF_UP);
        StatisticClassVO statisticClassVO = StatisticClassVO.builder()
                .averageScore(classAvg)
                .studentCount(studentCount)
                .highestScore(avgScores.get(studentArray.get(0)))
                .lowestScore(avgScores.get(studentArray.get(studentCount - 1)))
                .excellentRate(excellentRate)
                .build();
        List<StatisticClassVO.StudentScores> studentScores = new ArrayList<>();
        for(int i = 0;i<studentCount;i++){
            Student student = studentMap.get(studentArray.get(i));
            String studentId = student.getStudentId();
            String name = student.getName();
            String gender = student.getGender();
            BigDecimal avgScore = avgScores.get(studentId);
            StatisticClassVO.StudentScores studentScore = StatisticClassVO.StudentScores.builder()
                    .studentId(studentArray.get(i))
                    .name(name)
                    .gender(gender)
                    .averageScore(avgScore)
                    .rank(i + 1)
                    .build();
            studentScores.add(studentScore);
        }
        statisticClassVO.setStudentScores(studentScores);
        return statisticClassVO;
    }
}
