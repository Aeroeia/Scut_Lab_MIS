package com.mis.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticStudentVO {
    private String studentId;
    private String studentName;
    private BigDecimal averageScore;
    private Integer totalCourses;
    private List<CourseScores> courseScores;
    private ScoreDistribution scoreDistributions;
    @Data
    @Builder
    public static class CourseScores{
        private String courseId;
        private String courseName;
        private BigDecimal score;
    }
    @Data
    @Builder
    public static class ScoreDistribution{
        private Integer excellent;
        private Integer good;
        private Integer average;
        private Integer poor;
    }
}
