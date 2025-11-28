package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferVoucherEntry {

    /**
     * 分录ID
     */
    private Long entryId;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 借方金额
     */
    private BigDecimal debitAmount = BigDecimal.ZERO;

    /**
     * 贷方金额
     */
    private BigDecimal creditAmount = BigDecimal.ZERO;

    /**
     * 分录序号
     */
    private Integer entryNo;

    // 构造方法
    public TransferVoucherEntry(Long subjectId, String subjectCode, String subjectName,
                                String summary, BigDecimal debitAmount, BigDecimal creditAmount) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.summary = summary;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
    }

    /**
     * 获取金额（绝对值）
     */
    public BigDecimal getAmount() {
        return debitAmount.compareTo(BigDecimal.ZERO) > 0 ? debitAmount : creditAmount;
    }

    /**
     * 获取方向
     */
    public String getDirection() {
        return debitAmount.compareTo(BigDecimal.ZERO) > 0 ? "借" : "贷";
    }
}