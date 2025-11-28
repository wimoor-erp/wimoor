package com.wimoor.finance.closing.domain;

import com.wimoor.finance.voucher.domain.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDetail {

    /**
     * 期初余额
     */
    private BigDecimal beginBalance = BigDecimal.ZERO;

    /**
     * 期初余额方向
     */
    private Integer beginDirection; // 1-借, 2-贷

    /**
     * 本期借方合计
     */
    private BigDecimal debitTotal = BigDecimal.ZERO;

    /**
     * 本期贷方合计
     */
    private BigDecimal creditTotal = BigDecimal.ZERO;

    /**
     * 科目类型
     */
    private SubjectType subjectType;

    /**
     * 计算公式
     */
    private String formula;

    /**
     * 计算步骤
     */
    private List<CalculationStep> steps = new ArrayList<>();

    /**
     * 计算参数
     */
    private Map<String, Object> parameters = new HashMap<>();

    // 构造方法
    public CalculationDetail(BigDecimal beginBalance, Integer beginDirection,
                             BigDecimal debitTotal, BigDecimal creditTotal,
                             SubjectType subjectType) {
        this.beginBalance = beginBalance;
        this.beginDirection = beginDirection;
        this.debitTotal = debitTotal;
        this.creditTotal = creditTotal;
        this.subjectType = subjectType;
        this.formula = generateFormula();
        generateCalculationSteps();
    }

    /**
     * 生成计算公式
     */
    private String generateFormula() {
        if (subjectType.isDebitBalance()) {
            return "期末余额 = 期初余额 + 本期借方 - 本期贷方";
        } else {
            return "期末余额 = 期初余额 + 本期贷方 - 本期借方";
        }
    }

    /**
     * 生成计算步骤
     */
    private void generateCalculationSteps() {
        steps.clear();

        // 步骤1：期初余额
        steps.add(new CalculationStep(1, "期初余额",
                String.format("%s %.2f", beginDirection == 1 ? "借" : "贷", beginBalance),
                beginBalance));

        if (subjectType.isDebitBalance()) {
            // 步骤2：加借方
            steps.add(new CalculationStep(2, "加：本期借方",
                    String.format("借 %.2f", debitTotal), debitTotal));

            // 步骤3：减贷方
            steps.add(new CalculationStep(3, "减：本期贷方",
                    String.format("贷 %.2f", creditTotal), creditTotal.negate()));
        } else {
            // 步骤2：加贷方
            steps.add(new CalculationStep(2, "加：本期贷方",
                    String.format("贷 %.2f", creditTotal), creditTotal));

            // 步骤3：减借方
            steps.add(new CalculationStep(3, "减：本期借方",
                    String.format("借 %.2f", debitTotal), debitTotal.negate()));
        }
    }

    /**
     * 获取计算过程描述
     */
    public String getCalculationProcess() {
        return steps.stream()
                .map(CalculationStep::getDescription)
                .collect(Collectors.joining(" → "));
    }

    /**
     * 添加参数
     */
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    /**
     * 复制对象
     */
    public CalculationDetail copy() {
        CalculationDetail copy = new CalculationDetail();
        copy.beginBalance = this.beginBalance;
        copy.beginDirection = this.beginDirection;
        copy.debitTotal = this.debitTotal;
        copy.creditTotal = this.creditTotal;
        copy.subjectType = this.subjectType;
        copy.formula = this.formula;

        if (this.steps != null) {
            copy.steps = this.steps.stream()
                    .map(CalculationStep::copy)
                    .collect(Collectors.toList());
        }

        if (this.parameters != null) {
            copy.parameters = new HashMap<>(this.parameters);
        }

        return copy;
    }
}
