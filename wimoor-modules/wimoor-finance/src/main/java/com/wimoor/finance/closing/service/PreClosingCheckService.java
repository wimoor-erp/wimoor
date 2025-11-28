package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.ClosingCheckItem;
import com.wimoor.finance.closing.domain.PreClosingCheckResult;
import com.wimoor.finance.closing.domain.VoucherSequenceInfo;
import com.wimoor.finance.ledger.mapper.FinGeneralLedgerMapper;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.mapper.FinVouchersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PreClosingCheckService {

    @Autowired
    private FinVouchersMapper finVouchersMapper;

    @Autowired
    private FinGeneralLedgerMapper finGeneralLedgerMapper;

    @Autowired
    private EnhancedVoucherContinuityService enhancedVoucherContinuityService;
    @Autowired
    ProfitLossSubjectCheckService profitLossSubjectCheckService;
    /**
     * 执行全面结账前检查
     */
    public PreClosingCheckResult performComprehensiveCheck(String groupid, String period) {
        PreClosingCheckResult result = new PreClosingCheckResult();

        // 1. 检查未过账凭证
        checkUnpostedVouchers(groupid, period, result);

        // 2. 检查凭证连续性
        List<FinVouchers> vouchers = finVouchersMapper.selectVouchersByPeriod(groupid, period);
        enhancedVoucherContinuityService.checkEnhancedVoucherContinuity(vouchers, result);

        // 3. 检查总账平衡
        checkGeneralLedgerBalance(groupid, period, result);


        // 5. 检查损益类科目
        checkProfitLossSubjects(groupid, period, result);

        return result;
    }

    private void checkProfitLossSubjects(String groupid, String period, PreClosingCheckResult result) {
        profitLossSubjectCheckService.checkProfitLossSubjects(groupid, period, result);
    }

    /**
     * 检查未过账凭证
     */
    private void checkUnpostedVouchers(String groupid, String period, PreClosingCheckResult result) {
        Integer unpostedCount = finVouchersMapper.countUnpostedVouchers(groupid, period);

        ClosingCheckItem checkItem = new ClosingCheckItem();
        checkItem.setCheckItem("未过账凭证检查");

        if (unpostedCount > 0) {
            checkItem.setPassed(false);
            checkItem.setMessage("存在 " + unpostedCount + " 张未过账凭证");
            result.addError(checkItem.getMessage());
        } else {
            checkItem.setPassed(true);
            checkItem.setMessage("所有凭证均已过账");
        }

        result.addCheckItem(checkItem);
    }

    /**
     * 检查总账平衡
     */
    private void checkGeneralLedgerBalance(String groupid, String period, PreClosingCheckResult result) {
        // 计算资产总额
        BigDecimal totalAssets = calculateTotalBySubjectType(groupid, period, "ASSET");

        // 计算负债总额
        BigDecimal totalLiabilities = calculateTotalBySubjectType(groupid, period, "LIABILITY");

        // 计算权益总额
        BigDecimal totalEquity = calculateTotalBySubjectType(groupid, period, "EQUITY");

        ClosingCheckItem checkItem = new ClosingCheckItem();
        checkItem.setCheckItem("总账平衡检查");

        // 验证: 资产 = 负债 + 所有者权益
        BigDecimal rightSide = totalLiabilities.add(totalEquity);
        boolean isBalanced = totalAssets.compareTo(rightSide) == 0;

        if (isBalanced) {
            checkItem.setPassed(true);
            checkItem.setMessage("总账平衡: 资产(" + totalAssets + ") = 负债(" + totalLiabilities + ") + 权益(" + totalEquity + ")");
        } else {
            checkItem.setPassed(false);
            checkItem.setMessage("总账不平衡: 资产(" + totalAssets + ") ≠ 负债(" + totalLiabilities + ") + 权益(" + totalEquity + ")");
            result.addError(checkItem.getMessage());
        }

        result.addCheckItem(checkItem);
        result.setBalanced(isBalanced);
    }

    /**
     * 按科目类型计算总额
     */
    private BigDecimal calculateTotalBySubjectType(String groupid, String period, String subjectType) {
        List<Map<String, Object>> balances = finGeneralLedgerMapper.selectBalanceSummaryByType(groupid, period);

        return balances.stream()
                .filter(item -> subjectType.equals(item.get("subject_type")))
                .map(item -> (BigDecimal) item.get("end_balance"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}