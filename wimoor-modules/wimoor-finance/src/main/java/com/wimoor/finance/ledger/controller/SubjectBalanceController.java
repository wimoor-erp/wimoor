package com.wimoor.finance.ledger.controller;

import com.wimoor.finance.ledger.service.ISubjectBalanceService;
import com.wimoor.finance.report.domain.dto.BalanceQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

// 在控制器中使用
@RestController
@RequestMapping("/api/subject")
public class SubjectBalanceController {

    @Autowired
    private ISubjectBalanceService subjectBalanceService;

    @GetMapping("/balance")
    public BigDecimal getBalance(@RequestParam String groupid,
                                 @RequestParam String period,
                                 @RequestParam String subjectCode,
                                 @RequestParam(defaultValue = "END_BALANCE") String amountType) {
        return subjectBalanceService.getSubjectBalance(groupid, period, subjectCode, amountType);
    }

    @PostMapping("/balances")
    public Map<String, BigDecimal> getBalances(@RequestBody BalanceQueryRequest request) {
        return subjectBalanceService.getSubjectBalances(
                request.getGroupid(),
                request.getStartPeriod(),
                request.getSubjectCodes(),
                request.getAmountType()
        );
    }
}