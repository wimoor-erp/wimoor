package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedVoucherInfo {
    private Long voucherId;
    private String voucherNo;
    private String voucherType;
    private Integer entryCount;
}
