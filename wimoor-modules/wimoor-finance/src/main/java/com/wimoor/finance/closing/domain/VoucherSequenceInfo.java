package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.util.Date;

@Data
public class VoucherSequenceInfo {

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 凭证日期
     */
    private Date voucherDate;

    /**
     * 凭证状态
     */
    private Integer voucherStatus;

    /**
     * 序列号（从凭证编号中提取的数字）
     */
    private Integer sequenceNumber;

    /**
     * 获取格式化信息
     */
    public String getFormattedInfo() {
        return String.format("%s-%s(%d)", voucherType, voucherNo, sequenceNumber);
    }
}