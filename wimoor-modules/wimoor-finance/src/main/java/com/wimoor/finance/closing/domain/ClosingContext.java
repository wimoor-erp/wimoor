package com.wimoor.finance.closing.domain;

import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClosingContext {
    private String groupid;
    private String period;
    private String operator;
    private FinAccountingPeriods accountingPeriod;
    private PreClosingCheckResult preCheckResult;
    private ClosingReport report;
    private ProfitLossTransferResult transferResult;
    private List<ClosingVoucherInfo> generatedVouchers = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();

    public ClosingContext(String groupid, String period, String operator) {
        this.groupid = groupid;
        this.period = period;
        this.operator = operator;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
}
