package com.wimoor.finance.closing.domain;

import com.wimoor.finance.voucher.domain.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetail {

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
     * 科目类型
     */
    private SubjectType subjectType;

    /**
     * 结转金额
     */
    private BigDecimal transferAmount = BigDecimal.ZERO;

    /**
     * 结转前余额
     */
    private BigDecimal balanceBefore = BigDecimal.ZERO;

    /**
     * 结转后余额
     */
    private BigDecimal balanceAfter = BigDecimal.ZERO;

    /**
     * 结转时间
     */
    private Date transferTime = new Date();

    /**
     * 生成的凭证ID
     */
    private Long voucherId;

    /**
     * 生成的凭证编号
     */
    private String voucherNo;

    /**
     * 结转方向 (DEBIT/CREDIT)
     */
    private TransferDirection transferDirection;

    /**
     * 是否结转成功
     */
    private Boolean success = true;

    /**
     * 错误信息
     */
    private String errorMessage;

    private Boolean skipped = false;

    // 构造方法
    public TransferDetail(Long subjectId, String subjectCode, String subjectName,
                          SubjectType subjectType, BigDecimal transferAmount) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectType = subjectType;
        this.transferAmount = transferAmount;

        // 根据科目类型自动设置结转方向
        if (subjectType == SubjectType.INCOME) {
            this.transferDirection = TransferDirection.CREDIT; // 收入类贷方结转
        } else if (subjectType == SubjectType.EXPENSE) {
            this.transferDirection = TransferDirection.DEBIT; // 费用类借方结转
        }
    }

    /**
     * 设置结转失败
     */
    public void setFailure(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }

    /**
     * 获取结转描述
     */
    public String getTransferDescription() {
        String direction = transferDirection == TransferDirection.DEBIT ? "借" : "贷";
        return String.format("%s %s %.2f", subjectName, direction, transferAmount);
    }


}