package com.wimoor.finance.closing.domain;

import com.wimoor.finance.report.domain.dto.ReportGenerateResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ClosingReport {
    private String period;
    private String groupid;
        
    private Date generateTime;
    private String generatedBy;
    private String operator;

    // 统计信息
    private Integer totalVouchers;
    private Integer totalEntries;
    private BigDecimal totalVoucherAmount;

    // 科目统计
    private Integer totalSubjects;
    private Integer assetSubjects;
    private Integer liabilitySubjects;
    private Integer equitySubjects;
    private Integer incomeSubjects;
    private Integer expenseSubjects;

    // 余额信息
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal totalEquity;
    private BigDecimal netProfit;
    private ReportGenerateResponse balanceSheet;
    private ReportGenerateResponse assetSheet;
    private ReportGenerateResponse liabilitySheet;
    private ReportGenerateResponse equitySheet;
    private ReportGenerateResponse incomeSheet;
    private ReportGenerateResponse expenseSheet;
    private ReportGenerateResponse profitStatement;
    private ReportGenerateResponse cashFlowStatement;
    // 生成的凭证
    private List<ClosingVoucherInfo> generatedVouchers = new ArrayList<>();
     private LocalDateTime closingTime;
    // 检查结果
    private Boolean isBalanced;
    private List<ClosingCheckItem> checkItems = new ArrayList<>();

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }


}
