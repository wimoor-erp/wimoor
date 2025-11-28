package com.wimoor.finance.closing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferVoucherInfo {

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
    private String voucherType = "转";

    /**
     * 凭证日期
     */
    private Date voucherDate;

    /**
     * 凭证金额
     */
    private BigDecimal amount = BigDecimal.ZERO;

    /**
     * 凭证描述
     */
    private String description;

    /**
     * 凭证状态
     */
    private Integer voucherStatus;

    /**
     * 制单人
     */
    private String preparerBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 凭证分录列表
     */
    private List<TransferVoucherEntry> entries = new ArrayList<>();

    // 构造方法
    public TransferVoucherInfo(Long voucherId, String voucherNo, String description, BigDecimal amount) {
        this.voucherId = voucherId;
        this.voucherNo = voucherNo;
        this.description = description;
        this.amount = amount;
        this.voucherDate = new Date();
        this.createdTime = new Date();
    }

    /**
     * 添加凭证分录
     */
    public void addEntry(TransferVoucherEntry entry) {
        this.entries.add(entry);
    }

    /**
     * 获取凭证摘要
     */
    public String getVoucherSummary() {
        return String.format("%s %s %.2f", voucherNo, description, amount);
    }
}
