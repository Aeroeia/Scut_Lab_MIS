package com.mis.backend.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class StatisticCourseVO {
    private BigDecimal averageScore;
    private Integer studentCount;
    private BigDecimal highestScore;
    private BigDecimal lowestScore;
    private BigDecimal passRate;
    private BigDecimal excellentRate;
    private List<StudentScores> studentScores;
    private List<ClassScores> classScores;
    @Data
    @Builder
    public static class StudentScores{
        private String studentId;
        private String name;
        @JsonProperty("class")
        private String clazz;
        private BigDecimal score;
        private Integer rank;
    }
    @Data
    @Builder
    public static class ClassScores{
        private String className;
        private BigDecimal averageScore;
        private BigDecimal passRate;
    }

}
