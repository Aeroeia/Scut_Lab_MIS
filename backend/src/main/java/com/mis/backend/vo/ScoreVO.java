package com.mis.backend.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ScoreVO {
    BigDecimal score;
    Integer scoreId;
}
