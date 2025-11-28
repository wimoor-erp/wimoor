package com.wimoor.finance.closing.domain;

import com.wimoor.finance.voucher.domain.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndBalanceResult {

    /**
     * 期末余额
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 余额方向
     */
    private Integer direction; // 1-借, 2-贷

    /**
     * 计算方式
     */
    private CalculationMethod calculationMethod;

    /**
     * 计算明细
     */
    private CalculationDetail calculationDetail = new CalculationDetail();

    /**
     * 是否为零余额
     */
    private Boolean zeroBalance = false;

    /**
     * 余额状态
     */
    private BalanceStatus balanceStatus;

    /**
     * 计算时间
     */
    private Date calculateTime = new Date();

    /**
     * 验证结果
     */
    private ValidationResult validationResult;

    // 构造方法

    public EndBalanceResult(BigDecimal balance, Integer direction) {
        this.balance = balance;
        this.direction = direction;
        this.zeroBalance = balance.compareTo(BigDecimal.ZERO) == 0;
        this.balanceStatus = determineBalanceStatus(balance, direction);
    }

    public EndBalanceResult(BigDecimal beginBalance, Integer beginDirection,
                            BigDecimal debitTotal, BigDecimal creditTotal,
                            SubjectType subjectType) {
        this.calculationDetail = new CalculationDetail(beginBalance, beginDirection, debitTotal, creditTotal, subjectType);
        calculateBalance(beginBalance, beginDirection, debitTotal, creditTotal, subjectType);
    }

    // 业务方法

    /**
     * 计算期末余额
     */
    public void calculateBalance(BigDecimal beginBalance, Integer beginDirection,
                                 BigDecimal debitTotal, BigDecimal creditTotal,
                                 SubjectType subjectType) {
        this.calculationMethod = CalculationMethod.STANDARD;
        this.calculationDetail = new CalculationDetail(beginBalance, beginDirection, debitTotal, creditTotal, subjectType);

        BigDecimal endBalance;
        Integer endDirection;

        if (subjectType.isDebitBalance()) {
            // 借方余额科目：期初借方 + 本期借方 - 本期贷方
            BigDecimal effectiveBeginBalance = beginDirection == 1 ?
                    beginBalance : beginBalance.negate();
            endBalance = effectiveBeginBalance.add(debitTotal).subtract(creditTotal);
        } else {
            // 贷方余额科目：期初贷方 + 本期贷方 - 本期借方
            BigDecimal effectiveBeginBalance = beginDirection == 2 ?
                    beginBalance : beginBalance.negate();
            endBalance = effectiveBeginBalance.add(creditTotal).subtract(debitTotal);
        }

        // 确定余额方向
        if (endBalance.compareTo(BigDecimal.ZERO) == 0) {
            endDirection = subjectType.getDefaultDirection();
            this.zeroBalance = true;
        } else if (endBalance.compareTo(BigDecimal.ZERO) > 0) {
            endDirection = subjectType.isDebitBalance() ? 1 : 2;
            this.zeroBalance = false;
        } else {
            endDirection = subjectType.isDebitBalance() ? 2 : 1;
            endBalance = endBalance.abs();
            this.zeroBalance = false;
        }

        this.balance = endBalance;
        this.direction = endDirection;
        this.balanceStatus = determineBalanceStatus(endBalance, endDirection);
        this.calculateTime = new Date();

        // 验证计算结果
        validateCalculation();
    }

    /**
     * 确定余额状态
     */
    private BalanceStatus determineBalanceStatus(BigDecimal balance, Integer direction) {
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            return BalanceStatus.ZERO;
        }

        // 根据科目性质和余额方向判断状态
        if (direction == 1) { // 借方
            return BalanceStatus.DEBIT_BALANCE;
        } else { // 贷方
            return BalanceStatus.CREDIT_BALANCE;
        }
    }

    /**
     * 验证计算结果
     */
    private void validateCalculation() {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // 余额不能为负数（因为负数时我们已经取绝对值并调整方向）
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            errors.add("余额计算异常：余额为负数");
        }

        // 方向必须为1或2
        if (direction != 1 && direction != 2) {
            errors.add("余额方向必须为1(借)或2(贷)");
        }

        // 零余额检查
        if (zeroBalance && balance.compareTo(BigDecimal.ZERO) != 0) {
            errors.add("零余额标识与实际余额不一致");
        }

        // 大额余额警告
        if (balance.compareTo(new BigDecimal("1000000")) > 0) {
            warnings.add("余额金额较大，请确认");
        }

        this.validationResult = new ValidationResult(errors.isEmpty(),
                errors.isEmpty() ? null : String.join("; ", errors),
                warnings);
    }

    /**
     * 获取余额方向描述
     */
    public String getDirectionDescription() {
        return direction == 1 ? "借" : "贷";
    }

    /**
     * 获取带方向的余额（借方为正，贷方为负）
     */
    public BigDecimal getDirectedBalance() {
        return direction == 1 ? balance : balance.negate();
    }

    /**
     * 获取格式化余额显示
     */
    public String getFormattedBalance() {
        return String.format("%s %.2f", getDirectionDescription(), balance);
    }

    /**
     * 检查是否为正常余额（符合科目性质）
     */
    public boolean isNormalBalance(SubjectType subjectType) {
        if (zeroBalance) {
            return true; // 零余额总是正常的
        }

        if (subjectType.isDebitBalance()) {
            return direction == 1; // 借方科目应为借方余额
        } else {
            return direction == 2; // 贷方科目应为贷方余额
        }
    }

    /**
     * 获取余额异常描述（如果不正常）
     */
    public String getBalanceAbnormality(SubjectType subjectType) {
        if (isNormalBalance(subjectType)) {
            return null;
        }

        String expectedDirection = subjectType.isDebitBalance() ? "借方" : "贷方";
        String actualDirection = getDirectionDescription();

        return String.format("科目性质为%s，但余额方向为%s", expectedDirection, actualDirection);
    }

    /**
     * 复制对象
     */
    public EndBalanceResult copy() {
        EndBalanceResult copy = new EndBalanceResult();
        copy.balance = this.balance;
        copy.direction = this.direction;
        copy.calculationMethod = this.calculationMethod;
        copy.calculationDetail = this.calculationDetail != null ? this.calculationDetail.copy() : null;
        copy.zeroBalance = this.zeroBalance;
        copy.balanceStatus = this.balanceStatus;
        copy.calculateTime = this.calculateTime;
        copy.validationResult = this.validationResult != null ? this.validationResult.copy() : null;
        return copy;
    }

    /**
     * 转换为字符串表示
     */
    @Override
    public String toString() {
        return String.format("EndBalanceResult{balance=%.2f, direction=%s, status=%s}",
                balance, getDirectionDescription(), balanceStatus);
    }
}
