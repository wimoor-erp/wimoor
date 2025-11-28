package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClosingVoucherInfo {
    private Long voucherId;
    private String voucherNo;
    private String voucherType;
    private BigDecimal amount;
    private String description;
}
