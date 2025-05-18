package com.mis.backend.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StatisticClassDTO {
    private BigDecimal averageScore;
    private Integer studentCount;
    private BigDecimal highestScore;
    private BigDecimal lowestScore;
    private BigDecimal excellentRate;
    private List<StudentScores> studentScores;
    @Data
    @Builder
    public static class StudentScores{
        private String studentId;
        private String name;
        private String gender;
        private BigDecimal averageScore;
        private Integer rank;
    }
}
