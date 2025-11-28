package com.wimoor.finance.voucher.service.util;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceResult {

    private BigDecimal balance;
    private Integer direction;

    public BalanceResult(BigDecimal endBalance, Integer endDirection) {
        balance = endBalance;
        direction = endDirection;
    }

}
