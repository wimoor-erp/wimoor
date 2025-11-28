package com.wimoor.finance.ledger.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// SubjectBalanceDTO.java

public class SubjectBalanceDTO {
    private String subjectCode;
    private String subjectName;
    private Integer subjectType;
    private Integer direction;
    private BigDecimal beginBalance;
    private Integer beginDirection;
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private BigDecimal endBalance;
    private Integer endDirection;
    private String period;
    private Integer subjectLevel;
    private String parentSubjectCode;
    private Boolean enabled;

    // 构造方法
    public SubjectBalanceDTO() {
        this.beginBalance = BigDecimal.ZERO;
        this.debitTotal = BigDecimal.ZERO;
        this.creditTotal = BigDecimal.ZERO;
        this.endBalance = BigDecimal.ZERO;
    }

    // Getter 和 Setter 方法
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public BigDecimal getBeginBalance() {
        return beginBalance != null ? beginBalance : BigDecimal.ZERO;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public Integer getBeginDirection() {
        return beginDirection;
    }

    public void setBeginDirection(Integer beginDirection) {
        this.beginDirection = beginDirection;
    }

    public BigDecimal getDebitTotal() {
        return debitTotal != null ? debitTotal : BigDecimal.ZERO;
    }

    public void setDebitTotal(BigDecimal debitTotal) {
        this.debitTotal = debitTotal;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal != null ? creditTotal : BigDecimal.ZERO;
    }

    public void setCreditTotal(BigDecimal creditTotal) {
        this.creditTotal = creditTotal;
    }

    public BigDecimal getEndBalance() {
        return endBalance != null ? endBalance : BigDecimal.ZERO;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public Integer getEndDirection() {
        return endDirection;
    }

    public void setEndDirection(Integer endDirection) {
        this.endDirection = endDirection;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(Integer subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    public String getParentSubjectCode() {
        return parentSubjectCode;
    }

    public void setParentSubjectCode(String parentSubjectCode) {
        this.parentSubjectCode = parentSubjectCode;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // 业务方法

    /**
     * 计算本期净发生额
     */
    public BigDecimal getNetAmount() {
        if (direction == 1) { // 借方科目
            return getDebitTotal().subtract(getCreditTotal());
        } else { // 贷方科目
            return getCreditTotal().subtract(getDebitTotal());
        }
    }

    /**
     * 自动计算期末余额
     */
    public void calculateEndBalance() {
        BigDecimal netAmount = getNetAmount();

        // 根据科目方向计算期末余额
        if (direction != null && direction == 1) {
            // 借方科目：期初借方 + 本期借方 - 本期贷方
            this.endBalance = getBeginBalance().add(netAmount);
        } else if (direction != null && direction == -1) {
            // 贷方科目：期初贷方 + 本期贷方 - 本期借方
            this.endBalance = getBeginBalance().subtract(netAmount);
        } else {
            // 平或未知方向
            this.endBalance = getBeginBalance().add(netAmount);
        }

        // 设置期末余额方向
        if (this.endBalance.compareTo(BigDecimal.ZERO) > 0) {
            this.endDirection = 1; // 借方
        } else if (this.endBalance.compareTo(BigDecimal.ZERO) < 0) {
            this.endDirection = -1; // 贷方
            this.endBalance = this.endBalance.abs(); // 贷方余额通常显示为正数
        } else {
            this.endDirection = 0; // 平
        }
    }

    /**
     * 根据方向调整余额显示
     */
    public BigDecimal adjustBalanceByDirection(BigDecimal balance, Integer balanceDirection) {
        if (balance == null) {
            return BigDecimal.ZERO;
        }
        if (balanceDirection != null && balanceDirection == -1) {
            return balance.negate(); // 贷方余额显示为负数
        }
        return balance;
    }

    /**
     * 检查是否为末级科目
     */
    public boolean isLeafSubject() {
        return subjectLevel != null && subjectLevel > 0;
    }

    /**
     * 获取完整的科目显示信息
     */
    public String getDisplayName() {
        return subjectCode + " " + subjectName;
    }

    @Override
    public String toString() {
        return "SubjectBalanceDTO{" +
                "subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", period='" + period + '\'' +
                ", beginBalance=" + beginBalance +
                ", debitTotal=" + debitTotal +
                ", creditTotal=" + creditTotal +
                ", endBalance=" + endBalance +
                '}';
    }
}