package com.mis.backend.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardVO {
    private Integer studentCount;
    private Integer courseCount;
    private BigDecimal averageScore;
}
