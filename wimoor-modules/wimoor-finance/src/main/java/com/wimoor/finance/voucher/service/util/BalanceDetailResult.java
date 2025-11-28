package com.wimoor.finance.voucher.service.util;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDetailResult {

    private BigDecimal balance;
    private Integer direction;

    public BalanceDetailResult(BigDecimal currentBalance, Integer currentDirection) {
        this.balance = currentBalance;
        this.direction = currentDirection;
    }
}
