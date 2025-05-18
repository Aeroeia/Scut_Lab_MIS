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
public class StatisticClassVO {
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
