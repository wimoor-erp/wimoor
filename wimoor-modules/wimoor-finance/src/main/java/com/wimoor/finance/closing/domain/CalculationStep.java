package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationStep {

    /**
     * 步骤序号
     */
    private Integer stepNumber;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 步骤描述
     */
    private String description;

    /**
     * 步骤金额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 累计金额
     */
    private BigDecimal cumulativeAmount = BigDecimal.ZERO;

    // 构造方法
    public CalculationStep(Integer stepNumber, String stepName, String description, BigDecimal amount) {
        this.stepNumber = stepNumber;
        this.stepName = stepName;
        this.description = description;
        this.amount = amount;
    }

    /**
     * 获取完整描述
     */
    public String getFullDescription() {
        return String.format("%d. %s: %s (%.2f)", stepNumber, stepName, description, amount);
    }

    /**
     * 复制对象
     */
    public CalculationStep copy() {
        return new CalculationStep(stepNumber, stepName, description, amount, cumulativeAmount);
    }
}
