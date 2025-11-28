package com.wimoor.finance.ledger.service;

import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ISubjectBalanceService {
    /**
     * 获取所有科目余额
     *
     * @param groupid 集团ID
     * @param period 期间
     * @return 所有科目余额
     */
    Map<String,BigDecimal> getAllSubjectBalance(String groupid,String period);
    BigDecimal getSubjectBalance(String groupid, String period, String subjectCodes, String amountType);
    Map<String, BigDecimal> getSubjectBalances(String groupid, String period, List<String> subjectCodes, String amountType);
    List<FinDetailLedger> getSubjectDetails(String groupid, String period, String subjectCode);
    SubjectBalanceDTO getSubjectBalanceDetail(String groupid, String period, String subjectCode);
}
