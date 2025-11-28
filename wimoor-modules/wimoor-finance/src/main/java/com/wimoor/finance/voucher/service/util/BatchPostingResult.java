package com.wimoor.finance.voucher.service.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BatchPostingResult {
    private List<Long> successVoucherIds = new ArrayList<>();
    private Map<Long, String> failureVouchers = new HashMap<>();

    public void addSuccess(Long voucherId) {
        successVoucherIds.add(voucherId);
    }

    public void addFailure(Long voucherId, String error) {
        failureVouchers.put(voucherId, error);
    }

    public boolean isAllSuccess() {
        return failureVouchers.isEmpty();
    }



}
