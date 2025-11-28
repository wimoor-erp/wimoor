package com.wimoor.finance.closing.domain;

import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.voucher.domain.FinVouchers;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PreReverseCheckResult {
    private boolean passed = false;
    private String errorMessage;
    private List<String> warnings = new ArrayList<>();
    private FinAccountingPeriods accountingPeriod;
    private List<FinVouchers> modifiedVouchers = new ArrayList<>();

    public PreReverseCheckResult success() {
        this.passed = true;
        return this;
    }

    public PreReverseCheckResult fail(String errorMessage) {
        this.passed = false;
        this.errorMessage = errorMessage;
        return this;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
}