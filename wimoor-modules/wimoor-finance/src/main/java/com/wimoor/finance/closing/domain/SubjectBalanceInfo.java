package com.wimoor.finance.closing.domain;

import com.wimoor.finance.voucher.domain.SubjectType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubjectBalanceInfo {

    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private SubjectType subjectType;

    // 期初信息
    private BigDecimal beginBalance = BigDecimal.ZERO;
    private Integer beginDirection = 1; // 1-借, 2-贷

    // 本期发生额
    private BigDecimal debitTotal = BigDecimal.ZERO;
    private BigDecimal creditTotal = BigDecimal.ZERO;

    // 期末信息
    private BigDecimal endBalance = BigDecimal.ZERO;
    private Integer endDirection = 1; // 1-借, 2-贷

    /**
     * 获取正确的余额方向
     */
    public Integer getCorrectDirection() {
        if (subjectType == SubjectType.INCOME) {
            return 2; // 收入类应该是贷方余额
        } else if (subjectType == SubjectType.EXPENSE) {
            return 1; // 费用类应该是借方余额
        }
        return 1;
    }

    /**
     * 检查余额方向是否正确
     */
    public boolean isDirectionCorrect() {
        return endDirection.equals(getCorrectDirection());
    }

    /**
     * 获取发生额净额
     */
    public BigDecimal getNetActivity() {
        if (subjectType == SubjectType.INCOME) {
            return creditTotal.subtract(debitTotal); // 收入：贷-借
        } else if (subjectType == SubjectType.EXPENSE) {
            return debitTotal.subtract(creditTotal); // 费用：借-贷
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取格式化信息
     */
    public String getFormattedInfo() {
        String direction = endDirection == 1 ? "借" : "贷";
        return String.format("%s(%s): %s %.2f",
                subjectName, subjectCode, direction, endBalance);
    }
}