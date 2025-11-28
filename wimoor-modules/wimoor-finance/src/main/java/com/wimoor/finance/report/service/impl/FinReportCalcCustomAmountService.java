package com.wimoor.finance.report.service.impl;

import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.ledger.service.ISubjectBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class FinReportCalcCustomAmountService {
    @Autowired
    IFinGeneralLedgerService finGeneralLedgerService;

    public BigDecimal getCustomAmount(String groupid, String period, String customRule) {
        if (customRule == null || customRule.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        try {
            String[] parts = customRule.split(":");
            if (parts.length < 2 || !"CUSTOM".equals(parts[0])) {
                return BigDecimal.ZERO;
            }

            String ruleCode = parts[1];

            switch (ruleCode) {
                // 资产负债表相关
                case "ASSET_CURRENT":
                    return calculateCurrentAssets(groupid, period);
                case "ASSET_CURRENT_OTHER":
                    return calculateOtherCurrentAssets(groupid, period);
                case "ASSET_NON_CURRENT":
                    return calculateNonCurrentAssets(groupid, period);
                case "ASSET_NON_CURRENT_OTHER":
                    return calculateOtherNonCurrentAssets(groupid, period);
                case "LIABILITY_CURRENT":
                    return calculateCurrentLiabilities(groupid, period);
                case "LIABILITY_CURRENT_OTHER":
                    return calculateOtherCurrentLiabilities(groupid, period);
                case "LIABILITY_NON_CURRENT":
                    return calculateNonCurrentLiabilities(groupid, period);
                case "LIABILITY_NON_CURRENT_OTHER":
                    return calculateOtherNonCurrentLiabilities(groupid, period);
                case "EQUITY_OTHER":
                    return calculateOtherEquity(groupid, period);

                // 每股收益
                case "EPS_BASIC":
                    return calculateBasicEPS(groupid, period);
                case "EPS_DILUTED":
                    return calculateDilutedEPS(groupid, period);

                // 经营活动现金流
                case "CASH_IN_SALES":
                    return calculateCashFromSales(groupid, period);
                case "CASH_IN_TAX_REFUND":
                    return calculateCashFromTaxRefund(groupid, period);
                case "CASH_IN_OTHER_OPERATING":
                    return calculateOtherOperatingCashIn(groupid, period);
                case "CASH_OUT_PURCHASE":
                    return calculateCashOutForPurchase(groupid, period);
                case "CASH_OUT_SALARY":
                    return calculateCashOutForSalary(groupid, period);
                case "CASH_OUT_TAX":
                    return calculateCashOutForTax(groupid, period);
                case "CASH_OUT_OTHER_OPERATING":
                    return calculateOtherOperatingCashOut(groupid, period);

                // 投资活动现金流
                case "CASH_IN_INVEST_RECOVER":
                    return calculateCashFromInvestmentRecovery(groupid, period);
                case "CASH_IN_INVEST_INCOME":
                    return calculateCashFromInvestmentIncome(groupid, period);
                case "CASH_IN_ASSET_DISPOSE":
                    return calculateCashFromAssetDisposal(groupid, period);
                case "CASH_IN_OTHER_INVESTING":
                    return calculateOtherInvestingCashIn(groupid, period);
                case "CASH_OUT_INVEST":
                    return calculateCashOutForInvestment(groupid, period);
                case "CASH_OUT_INVEST_PAY":
                    return calculateCashOutForInvestmentPayment(groupid, period);
                case "CASH_OUT_OTHER_INVESTING":
                    return calculateOtherInvestingCashOut(groupid, period);

                // 筹资活动现金流
                case "CASH_IN_INVEST_ABSORB":
                    return calculateCashFromInvestmentAbsorption(groupid, period);
                case "CASH_IN_LOAN":
                    return calculateCashFromLoans(groupid, period);
                case "CASH_IN_OTHER_FINANCING":
                    return calculateOtherFinancingCashIn(groupid, period);
                case "CASH_OUT_LOAN_REPAY":
                    return calculateCashOutForLoanRepayment(groupid, period);
                case "CASH_OUT_DIVIDEND":
                    return calculateCashOutForDividends(groupid, period);
                case "CASH_OUT_INTEREST":
                    return calculateCashOutForInterest(groupid, period);
                case "CASH_OUT_OTHER_FINANCING":
                    return calculateOtherFinancingCashOut(groupid, period);

                // 现金及现金等价物
                case "CASH_EFFECT_EXCHANGE":
                    return calculateExchangeRateEffect(groupid, period);
                case "CASH_BEGINNING":
                    return calculateBeginningCashBalance(groupid, period);

                // 所有者权益变动
                case "BALANCE_CAPITAL_PRIOR":
                    return getPriorPeriodCapitalBalance(groupid, period);
                case "BALANCE_RESERVE_CAPITAL_PRIOR":
                    return getPriorPeriodCapitalReserveBalance(groupid, period);
                case "BALANCE_RESERVE_SURPLUS_PRIOR":
                    return getPriorPeriodSurplusReserveBalance(groupid, period);
                case "BALANCE_RETAINED_PRIOR":
                    return getPriorPeriodRetainedEarningsBalance(groupid, period);
                case "CHANGE_FAIR_VALUE":
                    return calculateFairValueChange(groupid, period);
                case "CHANGE_DIRECT_EQUITY_OTHER":
                    return calculateDirectEquityOtherChanges(groupid, period);
                case "CHANGE_CAPITAL_IN":
                    return calculateCapitalIncrease(groupid, period);
                case "CHANGE_CAPITAL_REDUCE":
                    return calculateCapitalReduction(groupid, period);
                case "CHANGE_SURPLUS_EXTRACT":
                    return calculateSurplusExtraction(groupid, period);
                case "CHANGE_DIVIDEND":
                    return calculateDividendDistribution(groupid, period);
                case "CHANGE_CAPITAL_SURPLUS":
                    return calculateCapitalSurplusChange(groupid, period);
                case "CHANGE_INTERNAL_TRANSFER_OTHER":
                    return calculateInternalTransferOther(groupid, period);

                default:
                    log.warn("未知的自定义规则: {}", customRule);
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            log.error("计算自定义规则失败: groupid={}, period={}, rule={}", groupid, period, customRule, e);
            return BigDecimal.ZERO;
        }
    }

    // ==================== 资产负债表相关方法 ====================
    private BigDecimal calculateCurrentAssets(String groupid, String period) {
        // 流动资产 = 货币资金 + 应收票据 + 应收账款 + 预付款项 + 存货 + 其他流动资产
        return getItemAmount(groupid, period, "ASSET_CASH")
                .add(getItemAmount(groupid, period, "ASSET_BILLS"))
                .add(getItemAmount(groupid, period, "ASSET_RECEIVABLE"))
                .add(getItemAmount(groupid, period, "ASSET_PREPAY"))
                .add(getItemAmount(groupid, period, "ASSET_INVENTORY"))
                .add(getItemAmount(groupid, period, "ASSET_CURRENT_OTHER"));
    }

    private BigDecimal calculateOtherCurrentAssets(String groupid, String period) {
        // 其他流动资产 = 待摊费用 + 预交税金 + 其他
        return getItemAmount(groupid, period, "ASSET_PREPAID_EXPENSE")
                .add(getItemAmount(groupid, period, "ASSET_PREPAID_TAX"))
                .add(getItemAmount(groupid, period, "ASSET_CURRENT_OTHER_MISC"));
    }

    private BigDecimal calculateNonCurrentAssets(String groupid, String period) {
        // 非流动资产 = 长期股权投资 + 固定资产 + 在建工程 + 无形资产 + 长期待摊费用 + 其他非流动资产
        return getItemAmount(groupid, period, "ASSET_LONG_INVEST")
                .add(getItemAmount(groupid, period, "ASSET_FIXED"))
                .add(getItemAmount(groupid, period, "ASSET_CONSTRUCTION"))
                .add(getItemAmount(groupid, period, "ASSET_INTANGIBLE"))
                .add(getItemAmount(groupid, period, "ASSET_DEFERRED"))
                .add(getItemAmount(groupid, period, "ASSET_NON_CURRENT_OTHER"));
    }

    private BigDecimal calculateOtherNonCurrentAssets(String groupid, String period) {
        // 其他非流动资产 = 长期应收款 + 递延所得税资产 + 其他
        return getItemAmount(groupid, period, "ASSET_LONG_RECEIVABLE")
                .add(getItemAmount(groupid, period, "ASSET_DEFERRED_TAX"))
                .add(getItemAmount(groupid, period, "ASSET_NON_CURRENT_OTHER_MISC"));
    }

    private BigDecimal calculateCurrentLiabilities(String groupid, String period) {
        // 流动负债 = 短期借款 + 应付票据 + 应付账款 + 预收款项 + 应付职工薪酬 + 应交税费 + 其他流动负债
        return getItemAmount(groupid, period, "LIABILITY_SHORT_LOAN")
                .add(getItemAmount(groupid, period, "LIABILITY_BILLS"))
                .add(getItemAmount(groupid, period, "LIABILITY_PAYABLE"))
                .add(getItemAmount(groupid, period, "LIABILITY_ADVANCE"))
                .add(getItemAmount(groupid, period, "LIABILITY_SALARY"))
                .add(getItemAmount(groupid, period, "LIABILITY_TAX"))
                .add(getItemAmount(groupid, period, "LIABILITY_CURRENT_OTHER"));
    }

    private BigDecimal calculateOtherCurrentLiabilities(String groupid, String period) {
        // 其他流动负债 = 应付利息 + 应付股利 + 其他应付款
        return getItemAmount(groupid, period, "LIABILITY_INTEREST_PAYABLE")
                .add(getItemAmount(groupid, period, "LIABILITY_DIVIDEND_PAYABLE"))
                .add(getItemAmount(groupid, period, "LIABILITY_OTHER_PAYABLE"));
    }

    private BigDecimal calculateNonCurrentLiabilities(String groupid, String period) {
        // 非流动负债 = 长期借款 + 应付债券 + 其他非流动负债
        return getItemAmount(groupid, period, "LIABILITY_LONG_LOAN")
                .add(getItemAmount(groupid, period, "LIABILITY_BONDS"))
                .add(getItemAmount(groupid, period, "LIABILITY_NON_CURRENT_OTHER"));
    }

    private BigDecimal calculateOtherNonCurrentLiabilities(String groupid, String period) {
        // 其他非流动负债 = 长期应付款 + 递延所得税负债 + 其他
        return getItemAmount(groupid, period, "LIABILITY_LONG_PAYABLE")
                .add(getItemAmount(groupid, period, "LIABILITY_DEFERRED_TAX"))
                .add(getItemAmount(groupid, period, "LIABILITY_NON_CURRENT_OTHER_MISC"));
    }

    private BigDecimal calculateOtherEquity(String groupid, String period) {
        // 其他所有者权益 = 其他权益工具 + 专项储备 + 其他综合收益
        return getItemAmount(groupid, period, "EQUITY_OTHER_INSTRUMENT")
                .add(getItemAmount(groupid, period, "EQUITY_SPECIAL_RESERVE"))
                .add(getItemAmount(groupid, period, "EQUITY_OTHER_COMPREHENSIVE"));
    }

    // ==================== 每股收益计算 ====================
    private BigDecimal calculateBasicEPS(String groupid, String period) {
        // 基本每股收益 = 归属于普通股股东的净利润 / 发行在外普通股的加权平均数
        BigDecimal netProfit = getItemAmount(groupid, period, "PROFIT_NET_PARENT");
        BigDecimal weightedShares = getWeightedAverageShares(groupid, period);

        if (weightedShares.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return netProfit.divide(weightedShares, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDilutedEPS(String groupid, String period) {
        // 稀释每股收益 = 调整后归属于普通股股东的净利润 / 调整后发行在外普通股的加权平均数
        BigDecimal adjustedNetProfit = getAdjustedNetProfit(groupid, period);
        BigDecimal dilutedWeightedShares = getDilutedWeightedAverageShares(groupid, period);

        if (dilutedWeightedShares.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return adjustedNetProfit.divide(dilutedWeightedShares, 4, RoundingMode.HALF_UP);
    }

    // ==================== 经营活动现金流 ====================
    private BigDecimal calculateCashFromSales(String groupid, String period) {
        // 销售商品、提供劳务收到的现金 = 营业收入 + 应交增值税(销项) + 应收账款的减少 + 预收账款的增加
        BigDecimal revenue = getItemAmount(groupid, period, "REVENUE_TOTAL");
        BigDecimal outputVAT = getItemAmount(groupid, period, "TAX_VAT_OUTPUT");
        BigDecimal arDecrease = getAccountsReceivableDecrease(groupid, period);
        BigDecimal advanceIncrease = getAdvanceReceiptsIncrease(groupid, period);

        return revenue.add(outputVAT).add(arDecrease).add(advanceIncrease);
    }

    private BigDecimal calculateCashFromTaxRefund(String groupid, String period) {
        // 收到的税费返还 = 所得税返还 + 增值税返还 + 其他税费返还
        return getItemAmount(groupid, period, "TAX_REFUND_INCOME")
                .add(getItemAmount(groupid, period, "TAX_REFUND_VAT"))
                .add(getItemAmount(groupid, period, "TAX_REFUND_OTHER"));
    }

    private BigDecimal calculateOtherOperatingCashIn(String groupid, String period) {
        // 收到其他与经营活动有关的现金 = 其他应收款的减少 + 其他应付款的增加 + 其他
        BigDecimal otherReceivableDecrease = getOtherReceivableDecrease(groupid, period);
        BigDecimal otherPayableIncrease = getOtherPayableIncrease(groupid, period);
        BigDecimal misc = getItemAmount(groupid, period, "CASH_IN_OPERATING_OTHER_MISC");

        return otherReceivableDecrease.add(otherPayableIncrease).add(misc);
    }

    private BigDecimal calculateCashOutForPurchase(String groupid, String period) {
        // 购买商品、接受劳务支付的现金 = 营业成本 + 应交增值税(进项) + 存货的增加 + 应付账款的减少 + 预付账款的增加
        BigDecimal cost = getItemAmount(groupid, period, "COST_TOTAL");
        BigDecimal inputVAT = getItemAmount(groupid, period, "TAX_VAT_INPUT");
        BigDecimal inventoryIncrease = getInventoryIncrease(groupid, period);
        BigDecimal apDecrease = getAccountsPayableDecrease(groupid, period);
        BigDecimal prepayIncrease = getPrepaymentsIncrease(groupid, period);

        return cost.add(inputVAT).add(inventoryIncrease).add(apDecrease).add(prepayIncrease);
    }

    private BigDecimal calculateCashOutForSalary(String groupid, String period) {
        // 支付给职工以及为职工支付的现金 = 应付职工薪酬的减少 + 本期计提的职工薪酬
        BigDecimal salaryPayableDecrease = getSalaryPayableDecrease(groupid, period);
        BigDecimal salaryAccrued = getItemAmount(groupid, period, "EXPENSE_SALARY_ACCRUED");

        return salaryPayableDecrease.add(salaryAccrued);
    }

    private BigDecimal calculateCashOutForTax(String groupid, String period) {
        // 支付的各项税费 = 所得税费用 + 应交所得税的减少 + 应交增值税的减少 + 其他税费
        BigDecimal incomeTaxExpense = getItemAmount(groupid, period, "EXPENSE_INCOME_TAX");
        BigDecimal incomeTaxPayableDecrease = getIncomeTaxPayableDecrease(groupid, period);
        BigDecimal vatPayableDecrease = getVATPayableDecrease(groupid, period);
        BigDecimal otherTax = getItemAmount(groupid, period, "TAX_OTHER_PAID");

        return incomeTaxExpense.add(incomeTaxPayableDecrease).add(vatPayableDecrease).add(otherTax);
    }

    private BigDecimal calculateOtherOperatingCashOut(String groupid, String period) {
        // 支付其他与经营活动有关的现金 = 销售费用 + 管理费用 + 研发费用 + 其他应收款的增加 + 其他应付款的减少
        BigDecimal sellingExpense = getItemAmount(groupid, period, "EXPENSE_SELLING");
        BigDecimal adminExpense = getItemAmount(groupid, period, "EXPENSE_ADMIN");
        BigDecimal rndExpense = getItemAmount(groupid, period, "EXPENSE_RND");
        BigDecimal otherReceivableIncrease = getOtherReceivableIncrease(groupid, period);
        BigDecimal otherPayableDecrease = getOtherPayableDecrease(groupid, period);

        return sellingExpense.add(adminExpense).add(rndExpense)
                .add(otherReceivableIncrease).add(otherPayableDecrease);
    }

    // ==================== 投资活动现金流 ====================
    private BigDecimal calculateCashFromInvestmentRecovery(String groupid, String period) {
        // 收回投资收到的现金 = 交易性金融资产的减少 + 可供出售金融资产的减少
        BigDecimal financialAssetDecrease = getFinancialAssetDecrease(groupid, period);
        BigDecimal availableSaleDecrease = getAvailableForSaleDecrease(groupid, period);

        return financialAssetDecrease.add(availableSaleDecrease);
    }

    private BigDecimal calculateCashFromInvestmentIncome(String groupid, String period) {
        // 取得投资收益收到的现金 = 投资收益 + 应收股利的减少 + 应收利息的减少
        BigDecimal investmentIncome = getItemAmount(groupid, period, "INCOME_INVESTMENT");
        BigDecimal dividendReceivableDecrease = getDividendReceivableDecrease(groupid, period);
        BigDecimal interestReceivableDecrease = getInterestReceivableDecrease(groupid, period);

        return investmentIncome.add(dividendReceivableDecrease).add(interestReceivableDecrease);
    }

    private BigDecimal calculateCashFromAssetDisposal(String groupid, String period) {
        // 处置固定资产、无形资产和其他长期资产收回的现金净额
        return getItemAmount(groupid, period, "CASH_IN_ASSET_DISPOSAL_NET");
    }

    private BigDecimal calculateOtherInvestingCashIn(String groupid, String period) {
        // 收到其他与投资活动有关的现金
        return getItemAmount(groupid, period, "CASH_IN_INVESTING_OTHER");
    }

    private BigDecimal calculateCashOutForInvestment(String groupid, String period) {
        // 购建固定资产、无形资产和其他长期资产支付的现金
        return getItemAmount(groupid, period, "CASH_OUT_FIXED_ASSET")
                .add(getItemAmount(groupid, period, "CASH_OUT_INTANGIBLE_ASSET"))
                .add(getItemAmount(groupid, period, "CASH_OUT_OTHER_LONG_ASSET"));
    }

    private BigDecimal calculateCashOutForInvestmentPayment(String groupid, String period) {
        // 投资支付的现金 = 交易性金融资产的增加 + 可供出售金融资产的增加 + 长期股权投资的增加
        BigDecimal financialAssetIncrease = getFinancialAssetIncrease(groupid, period);
        BigDecimal availableSaleIncrease = getAvailableForSaleIncrease(groupid, period);
        BigDecimal longInvestIncrease = getLongTermInvestmentIncrease(groupid, period);

        return financialAssetIncrease.add(availableSaleIncrease).add(longInvestIncrease);
    }

    private BigDecimal calculateOtherInvestingCashOut(String groupid, String period) {
        // 支付其他与投资活动有关的现金
        return getItemAmount(groupid, period, "CASH_OUT_INVESTING_OTHER");
    }

    // ==================== 筹资活动现金流 ====================
    private BigDecimal calculateCashFromInvestmentAbsorption(String groupid, String period) {
        // 吸收投资收到的现金 = 实收资本的增加 + 资本公积的增加
        BigDecimal capitalIncrease = getCapitalIncreaseAmount(groupid, period);
        BigDecimal reserveIncrease = getCapitalReserveIncrease(groupid, period);

        return capitalIncrease.add(reserveIncrease);
    }

    private BigDecimal calculateCashFromLoans(String groupid, String period) {
        // 取得借款收到的现金 = 短期借款的增加 + 长期借款的增加
        BigDecimal shortLoanIncrease = getShortTermLoanIncrease(groupid, period);
        BigDecimal longLoanIncrease = getLongTermLoanIncrease(groupid, period);

        return shortLoanIncrease.add(longLoanIncrease);
    }

    private BigDecimal calculateOtherFinancingCashIn(String groupid, String period) {
        // 收到其他与筹资活动有关的现金
        return getItemAmount(groupid, period, "CASH_IN_FINANCING_OTHER");
    }

    private BigDecimal calculateCashOutForLoanRepayment(String groupid, String period) {
        // 偿还债务支付的现金 = 短期借款的减少 + 长期借款的减少
        BigDecimal shortLoanDecrease = getShortTermLoanDecrease(groupid, period);
        BigDecimal longLoanDecrease = getLongTermLoanDecrease(groupid, period);

        return shortLoanDecrease.add(longLoanDecrease);
    }

    private BigDecimal calculateCashOutForDividends(String groupid, String period) {
        // 分配股利、利润或偿付利息支付的现金 = 应付股利的减少 + 宣告发放的现金股利
        BigDecimal dividendPayableDecrease = getDividendPayableDecrease(groupid, period);
        BigDecimal dividendDeclared = getItemAmount(groupid, period, "DIVIDEND_DECLARED");

        return dividendPayableDecrease.add(dividendDeclared);
    }

    private BigDecimal calculateCashOutForInterest(String groupid, String period) {
        // 偿付利息支付的现金 = 应付利息的减少 + 本期计提的利息费用
        BigDecimal interestPayableDecrease = getInterestPayableDecrease(groupid, period);
        BigDecimal interestExpense = getItemAmount(groupid, period, "EXPENSE_INTEREST");

        return interestPayableDecrease.add(interestExpense);
    }

    private BigDecimal calculateOtherFinancingCashOut(String groupid,String period) {
        // 支付其他与筹资活动有关的现金
        return getItemAmount(groupid, period, "CASH_OUT_FINANCING_OTHER");
    }

    // ==================== 现金及现金等价物 ====================
    private BigDecimal calculateExchangeRateEffect(String groupid, String period) {
        // 汇率变动对现金的影响
        return getItemAmount(groupid, period, "CASH_EFFECT_EXCHANGE_RATE");
    }

    private BigDecimal calculateBeginningCashBalance(String groupid, String period) {
        // 期初现金及现金等价物余额 = 上期期末的货币资金
        String priorPeriod = getPriorPeriod(period);
        return getItemAmount(groupid, priorPeriod, "ASSET_CASH");
    }

    // ==================== 所有者权益变动 ====================
    private BigDecimal getPriorPeriodCapitalBalance(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        return getItemAmount(groupid, priorPeriod, "EQUITY_CAPITAL");
    }

    private BigDecimal getPriorPeriodCapitalReserveBalance(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        return getItemAmount(groupid, priorPeriod, "EQUITY_RESERVE");
    }

    private BigDecimal getPriorPeriodSurplusReserveBalance(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        return getItemAmount(groupid, priorPeriod, "EQUITY_SURPLUS");
    }

    private BigDecimal getPriorPeriodRetainedEarningsBalance(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        return getItemAmount(groupid, priorPeriod, "EQUITY_RETAINED");
    }

    private BigDecimal calculateFairValueChange(String groupid, String period) {
        // 公允价值变动 = 交易性金融资产公允价值变动 + 投资性房地产公允价值变动
        return getItemAmount(groupid, period, "FAIR_VALUE_FINANCIAL")
                .add(getItemAmount(groupid, period, "FAIR_VALUE_INVESTMENT_PROPERTY"));
    }

    private BigDecimal calculateDirectEquityOtherChanges(String groupid, String period) {
        // 直接计入所有者权益的利得和损失 = 其他综合收益
        return getItemAmount(groupid, period, "EQUITY_OTHER_COMPREHENSIVE");
    }

    private BigDecimal calculateCapitalIncrease(String groupid, String period) {
        // 资本投入增加 = 本期实收资本增加额
        return getCapitalIncreaseAmount(groupid, period);
    }

    private BigDecimal calculateCapitalReduction(String groupid, String period) {
        // 资本减少 = 本期实收资本减少额
        return getItemAmount(groupid, period, "EQUITY_CAPITAL_REDUCTION");
    }

    private BigDecimal calculateSurplusExtraction(String groupid, String period) {
        // 盈余公积提取 = 本期提取的盈余公积
        return getItemAmount(groupid, period, "EQUITY_SURPLUS_EXTRACT");
    }

    private BigDecimal calculateDividendDistribution(String groupid, String period) {
        // 利润分配 = 本期分配的现金股利
        return getItemAmount(groupid, period, "DIVIDEND_CASH_PAID");
    }

    private BigDecimal calculateCapitalSurplusChange(String groupid, String period) {
        // 资本公积变动 = 本期资本公积增加额 - 本期资本公积减少额
        BigDecimal reserveIncrease = getCapitalReserveIncrease(groupid, period);
        BigDecimal reserveDecrease = getItemAmount(groupid, period, "EQUITY_RESERVE_DECREASE");

        return reserveIncrease.subtract(reserveDecrease);
    }

    private BigDecimal calculateInternalTransferOther(String groupid, String period) {
        // 内部结转其他 = 盈余公积弥补亏损 + 其他内部结转
        BigDecimal surplusOffset = getItemAmount(groupid, period, "SURPLUS_OFFSET_LOSS");
        BigDecimal otherInternal = getItemAmount(groupid, period, "INTERNAL_TRANSFER_OTHER");

        return surplusOffset.add(otherInternal);
    }


    private String getPriorPeriod(String period) {
        // 根据当前期间计算上期期间
        try {
            if (period.length() == 6) { // YYYYMM 格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
                LocalDate currentDate = LocalDate.parse(period + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
                LocalDate priorDate = currentDate.minusMonths(1);
                return priorDate.format(formatter);
            } else if (period.length() == 4) { // YYYY 格式
                int year = Integer.parseInt(period);
                return String.valueOf(year - 1);
            }
        } catch (Exception e) {
            log.error("计算上期期间失败: {}", period, e);
        }
        return period;
    }

    // 各种变动计算的辅助方法
    private BigDecimal getAccountsReceivableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_RECEIVABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_RECEIVABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getAdvanceReceiptsIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_ADVANCE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_ADVANCE");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getInventoryIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_INVENTORY");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_INVENTORY");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getAccountsPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_PAYABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_PAYABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getPrepaymentsIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_PREPAY");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_PREPAY");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getSalaryPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_SALARY");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_SALARY");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getIncomeTaxPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_TAX_INCOME");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_TAX_INCOME");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getVATPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_TAX_VAT");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_TAX_VAT");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getOtherReceivableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_OTHER_RECEIVABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_OTHER_RECEIVABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getOtherPayableIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_OTHER_PAYABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_OTHER_PAYABLE");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getOtherReceivableIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_OTHER_RECEIVABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_OTHER_RECEIVABLE");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getOtherPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_OTHER_PAYABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_OTHER_PAYABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getFinancialAssetDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_FINANCIAL");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_FINANCIAL");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getAvailableForSaleDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_AVAILABLE_SALE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_AVAILABLE_SALE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getDividendReceivableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_DIVIDEND_RECEIVABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_DIVIDEND_RECEIVABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getInterestReceivableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_INTEREST_RECEIVABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_INTEREST_RECEIVABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getFinancialAssetIncrease(String groupid, String period) {
String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_FINANCIAL");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_FINANCIAL");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getAvailableForSaleIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_AVAILABLE_SALE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_AVAILABLE_SALE");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getLongTermInvestmentIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "ASSET_LONG_INVEST");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "ASSET_LONG_INVEST");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getCapitalIncreaseAmount(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "EQUITY_CAPITAL");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "EQUITY_CAPITAL");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getCapitalReserveIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "EQUITY_RESERVE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "EQUITY_RESERVE");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getShortTermLoanIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_SHORT_LOAN");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_SHORT_LOAN");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getLongTermLoanIncrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_LONG_LOAN");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_LONG_LOAN");
        return current.subtract(prior).max(BigDecimal.ZERO);
    }

    private BigDecimal getShortTermLoanDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_SHORT_LOAN");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_SHORT_LOAN");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getLongTermLoanDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_LONG_LOAN");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_LONG_LOAN");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getDividendPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_DIVIDEND_PAYABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_DIVIDEND_PAYABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getInterestPayableDecrease(String groupid, String period) {
        String priorPeriod = getPriorPeriod(period);
        BigDecimal current = getItemAmount(groupid, period, "LIABILITY_INTEREST_PAYABLE");
        BigDecimal prior = getItemAmount(groupid, priorPeriod, "LIABILITY_INTEREST_PAYABLE");
        return prior.subtract(current).max(BigDecimal.ZERO);
    }

    private BigDecimal getWeightedAverageShares(String groupid, String period) {
        // 发行在外普通股的加权平均数
        // 需要根据股本变动情况计算
        return BigDecimal.valueOf(1000000); // 示例值
    }

    private BigDecimal getAdjustedNetProfit(String groupid, String period) {
        // 调整后归属于普通股股东的净利润
        // 需要考虑可转换债券、认股权证等稀释性潜在普通股的影响
        BigDecimal netProfit = getItemAmount(groupid, period, "PROFIT_NET_PARENT");
        BigDecimal dilutionAdjustment = getItemAmount(groupid, period, "DILUTION_ADJUSTMENT");
        return netProfit.add(dilutionAdjustment);
    }

    private BigDecimal getDilutedWeightedAverageShares(String groupid, String period) {
        // 调整后发行在外普通股的加权平均数
        // 需要考虑所有稀释性潜在普通股转换为普通股的情况
        BigDecimal basicShares = getWeightedAverageShares(groupid, period);
        BigDecimal dilutionShares = getItemAmount(groupid, period, "DILUTION_SHARES");
        return basicShares.add(dilutionShares);
    }

    private BigDecimal calculateItemAmount(String groupid, String period, String subjectCodes) {
        // 这里调用科目余额服务获取数据
        // 简化实现，返回模拟数据
        FinGeneralLedger generalLedger=finGeneralLedgerService.selectBySubjectCodeAndPeriod(groupid, subjectCodes, period);
        return generalLedger!=null?generalLedger.getEndBalance():BigDecimal.ZERO;
    }
    // 修改后的 getItemAmount 方法，基于会计科目进行公式计算
    private BigDecimal getItemAmount(String groupid, String period, String itemCode) {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        try {
            // 根据科目代码进行公式计算
            switch (itemCode) {
                // ==================== 资产负债表科目 ====================
                case "ASSET_CASH":
                    // 货币资金 = 库存现金 + 银行存款 + 其他货币资金
                    return calculateItemAmount(groupid, period, "1001")  // 库存现金
                            .add(calculateItemAmount(groupid, period, "1002"))  // 银行存款
                            .add(calculateItemAmount(groupid, period, "1012")); // 其他货币资金

                case "ASSET_BILLS":
                    // 应收票据
                    return calculateItemAmount(groupid, period, "1121");

                case "ASSET_RECEIVABLE":
                    // 应收账款 = 应收账款 - 坏账准备
                    return calculateItemAmount(groupid, period, "1122")
                            .subtract(calculateItemAmount(groupid, period, "1231"));

                case "ASSET_PREPAY":
                    // 预付款项
                    return calculateItemAmount(groupid, period, "1123");

                case "ASSET_INVENTORY":
                    // 存货 = 原材料 + 库存商品 + 生产成本等
                    return calculateItemAmount(groupid, period, "1403")  // 原材料
                            .add(calculateItemAmount(groupid, period, "1405"))  // 库存商品
                            .add(calculateItemAmount(groupid, period, "5001"))  // 生产成本
                            .subtract(calculateItemAmount(groupid, period, "1471")); // 存货跌价准备

                case "ASSET_FINANCIAL":
                    // 交易性金融资产
                    return calculateItemAmount(groupid, period, "1101");

                case "ASSET_LONG_INVEST":
                    // 长期股权投资
                    return calculateItemAmount(groupid, period, "1511");

                case "ASSET_FIXED":
                    // 固定资产 = 固定资产原值 - 累计折旧 - 固定资产减值准备
                    return calculateItemAmount(groupid, period, "1601")  // 固定资产
                            .subtract(calculateItemAmount(groupid, period, "1602"))  // 累计折旧
                            .subtract(calculateItemAmount(groupid, period, "1603")); // 固定资产减值准备

                case "ASSET_CONSTRUCTION":
                    // 在建工程
                    return calculateItemAmount(groupid, period, "1604");

                case "ASSET_INTANGIBLE":
                    // 无形资产 = 无形资产 - 累计摊销 - 无形资产减值准备
                    return calculateItemAmount(groupid, period, "1701")  // 无形资产
                            .subtract(calculateItemAmount(groupid, period, "1702"))  // 累计摊销
                            .subtract(calculateItemAmount(groupid, period, "1703")); // 无形资产减值准备

                case "ASSET_DEFERRED":
                    // 长期待摊费用
                    return calculateItemAmount(groupid, period, "1801");

                // ==================== 负债类科目 ====================
                case "LIABILITY_SHORT_LOAN":
                    // 短期借款
                    return calculateItemAmount(groupid, period, "2001");

                case "LIABILITY_BILLS":
                    // 应付票据
                    return calculateItemAmount(groupid, period, "2201");

                case "LIABILITY_PAYABLE":
                    // 应付账款
                    return calculateItemAmount(groupid, period, "2202");

                case "LIABILITY_ADVANCE":
                    // 预收款项
                    return calculateItemAmount(groupid, period, "2203");

                case "LIABILITY_SALARY":
                    // 应付职工薪酬
                    return calculateItemAmount(groupid, period, "2211");

                case "LIABILITY_TAX":
                    // 应交税费
                    return calculateItemAmount(groupid, period, "2221");

                case "LIABILITY_LONG_LOAN":
                    // 长期借款
                    return calculateItemAmount(groupid, period, "2501");

                case "LIABILITY_BONDS":
                    // 应付债券
                    return calculateItemAmount(groupid, period, "2502");

                // ==================== 所有者权益类科目 ====================
                case "EQUITY_CAPITAL":
                    // 实收资本
                    return calculateItemAmount(groupid, period, "4001");

                case "EQUITY_RESERVE":
                    // 资本公积
                    return calculateItemAmount(groupid, period, "4002");

                case "EQUITY_SURPLUS":
                    // 盈余公积
                    return calculateItemAmount(groupid, period, "4101");

                case "EQUITY_RETAINED":
                    // 未分配利润
                    return calculateItemAmount(groupid, period, "4104");

                // ==================== 损益类科目 ====================
                case "REVENUE_TOTAL":
                    // 营业收入 = 主营业务收入 + 其他业务收入
                    return calculateItemAmount(groupid, period, "6001")  // 主营业务收入
                            .add(calculateItemAmount(groupid, period, "6051")); // 其他业务收入

                case "COST_TOTAL":
                    // 营业成本 = 主营业务成本 + 其他业务成本
                    return calculateItemAmount(groupid, period, "6401")  // 主营业务成本
                            .add(calculateItemAmount(groupid, period, "6402")); // 其他业务成本

                case "EXPENSE_SELLING":
                    // 销售费用
                    return calculateItemAmount(groupid, period, "6601");

                case "EXPENSE_ADMIN":
                    // 管理费用
                    return calculateItemAmount(groupid, period, "6602");

                case "EXPENSE_RND":
                    // 研发费用
                    return calculateItemAmount(groupid, period, "6603");

                case "EXPENSE_FINANCIAL":
                    // 财务费用
                    return calculateItemAmount(groupid, period, "6604");

                case "INCOME_INVESTMENT":
                    // 投资收益
                    return calculateItemAmount(groupid, period, "6111");

                case "EXPENSE_INCOME_TAX":
                    // 所得税费用
                    return calculateItemAmount(groupid, period, "6801");

                case "PROFIT_NET_PARENT":
                    // 归属于母公司股东的净利润 = 利润总额 - 所得税费用
                    BigDecimal totalProfit = calculateItemAmount(groupid, period, "6301")  // 营业外收入
                            .subtract(calculateItemAmount(groupid, period, "6711"))  // 营业外支出
                            .add(calculateItemAmount(groupid, period, "6111"))  // 投资收益
                            .add(calculateItemAmount(groupid, period, "6001"))  // 主营业务收入
                            .add(calculateItemAmount(groupid, period, "6051"))  // 其他业务收入
                            .subtract(calculateItemAmount(groupid, period, "6401"))  // 主营业务成本
                            .subtract(calculateItemAmount(groupid, period, "6402"))  // 其他业务成本
                            .subtract(calculateItemAmount(groupid, period, "6601"))  // 销售费用
                            .subtract(calculateItemAmount(groupid, period, "6602"))  // 管理费用
                            .subtract(calculateItemAmount(groupid, period, "6603"))  // 研发费用
                            .subtract(calculateItemAmount(groupid, period, "6604")); // 财务费用
                    return totalProfit.subtract(calculateItemAmount(groupid, period, "6801"));

                // ==================== 现金流相关科目 ====================
                case "TAX_VAT_OUTPUT":
                    // 应交增值税-销项税额
                    return calculateItemAmount(groupid, period, "22210102");

                case "TAX_VAT_INPUT":
                    // 应交增值税-进项税额
                    return calculateItemAmount(groupid, period, "22210101");

                case "TAX_REFUND_INCOME":
                    // 所得税返还
                    return calculateItemAmount(groupid, period, "6802");

                case "TAX_REFUND_VAT":
                    // 增值税返还
                    return calculateItemAmount(groupid, period, "630101");

                // ==================== 其他辅助科目 ====================
                case "ASSET_PREPAID_EXPENSE":
                    // 待摊费用
                    return calculateItemAmount(groupid, period, "1301");

                case "ASSET_PREPAID_TAX":
                    // 预交税金
                    return calculateItemAmount(groupid, period, "2225");

                case "ASSET_OTHER_RECEIVABLE":
                    // 其他应收款
                    return calculateItemAmount(groupid, period, "1221");

                case "LIABILITY_OTHER_PAYABLE":
                    // 其他应付款
                    return calculateItemAmount(groupid, period, "2241");

                case "LIABILITY_INTEREST_PAYABLE":
                    // 应付利息
                    return calculateItemAmount(groupid, period, "2231");

                case "LIABILITY_DIVIDEND_PAYABLE":
                    // 应付股利
                    return calculateItemAmount(groupid, period, "2232");

                case "ASSET_DIVIDEND_RECEIVABLE":
                    // 应收股利
                    return calculateItemAmount(groupid, period, "1131");

                case "ASSET_INTEREST_RECEIVABLE":
                    // 应收利息
                    return calculateItemAmount(groupid, period, "1132");

                case "ASSET_AVAILABLE_SALE":
                    // 可供出售金融资产
                    return calculateItemAmount(groupid, period, "1503");

                case "EXPENSE_SALARY_ACCRUED":
                    // 计提的职工薪酬
                    return calculateItemAmount(groupid, period, "221101");

                case "DIVIDEND_DECLARED":
                    // 宣告发放的现金股利
                    return calculateItemAmount(groupid, period, "223201");

                case "EXPENSE_INTEREST":
                    // 利息费用
                    return calculateItemAmount(groupid, period, "660301");

                case "DIVIDEND_CASH_PAID":
                    // 支付的现金股利
                    return calculateItemAmount(groupid, period, "223202");

                case "EQUITY_CAPITAL_REDUCTION":
                    // 实收资本减少
                    return calculateItemAmount(groupid, period, "400101");

                case "EQUITY_SURPLUS_EXTRACT":
                    // 提取盈余公积
                    return calculateItemAmount(groupid, period, "410101");

                case "EQUITY_RESERVE_DECREASE":
                    // 资本公积减少
                    return calculateItemAmount(groupid, period, "400201");

                case "SURPLUS_OFFSET_LOSS":
                    // 盈余公积弥补亏损
                    return calculateItemAmount(groupid, period, "410102");

                // ==================== 稀释每股收益相关 ====================
                case "DILUTION_ADJUSTMENT":
                    // 稀释调整 = 可转换债券利息 * (1 - 所得税率)
                    BigDecimal convertibleInterest = calculateItemAmount(groupid, period, "250101")  // 可转换债券利息
                            .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(0.25))); // 假设所得税率25%
                    return convertibleInterest;

                case "DILUTION_SHARES":
                    // 稀释股份 = 可转换债券转换股份 + 认股权证行权股份
                    return calculateItemAmount(groupid, period, "400301")  // 可转换债券转换
                            .add(calculateItemAmount(groupid, period, "400302")); // 认股权证行权

                // ==================== 其他现金流相关 ====================
                case "CASH_IN_ASSET_DISPOSAL_NET":
                    // 处置资产收回现金净额
                    return calculateItemAmount(groupid, period, "630102");

                case "CASH_IN_INVESTING_OTHER":
                    // 其他投资活动现金流入
                    return calculateItemAmount(groupid, period, "611101");

                case "CASH_OUT_FIXED_ASSET":
                    // 购建固定资产支付的现金
                    return calculateItemAmount(groupid, period, "160101");

                case "CASH_OUT_INTANGIBLE_ASSET":
                    // 购建无形资产支付的现金
                    return calculateItemAmount(groupid, period, "170101");

                case "CASH_OUT_INVESTING_OTHER":
                    // 其他投资活动现金流出
                    return calculateItemAmount(groupid, period, "611102");

                case "CASH_IN_FINANCING_OTHER":
                    // 其他筹资活动现金流入
                    return calculateItemAmount(groupid, period, "630103");

                case "CASH_OUT_FINANCING_OTHER":
                    // 其他筹资活动现金流出
                    return calculateItemAmount(groupid, period, "671101");

                case "CASH_EFFECT_EXCHANGE_RATE":
                    // 汇率变动对现金的影响
                    return calculateItemAmount(groupid, period, "630104");
                case "LIABILITY_LONG_PAYABLE":
                    // 长期应付款
                    return calculateItemAmount(groupid, period, "2701");

                case "LIABILITY_DEFERRED_TAX":
                    // 递延所得税负债
                    return calculateItemAmount(groupid, period, "2901");

                case "LIABILITY_NON_CURRENT_OTHER_MISC":
                    // 其他非流动负债-其他
                    return calculateItemAmount(groupid, period, "2702");

                case "ASSET_CURRENT_OTHER_MISC":
                    // 其他流动资产-其他
                    return calculateItemAmount(groupid, period, "1401");

                case "ASSET_LONG_RECEIVABLE":
                    // 长期应收款
                    return calculateItemAmount(groupid, period, "1531");

                case "ASSET_DEFERRED_TAX":
                    // 递延所得税资产
                    return calculateItemAmount(groupid, period, "1811");

                case "ASSET_NON_CURRENT_OTHER_MISC":
                    // 其他非流动资产-其他
                    return calculateItemAmount(groupid, period, "1901");

                case "EQUITY_OTHER_INSTRUMENT":
                    // 其他权益工具
                    return calculateItemAmount(groupid, period, "4003");

                case "EQUITY_SPECIAL_RESERVE":
                    // 专项储备
                    return calculateItemAmount(groupid, period, "4102");

                case "EQUITY_OTHER_COMPREHENSIVE":
                    // 其他综合收益
                    return calculateItemAmount(groupid, period, "4103");

                case "LIABILITY_CURRENT_OTHER":
                    // 其他流动负债
                    return calculateItemAmount(groupid, period, "224101");

                case "LIABILITY_NON_CURRENT_OTHER":
                    // 其他非流动负债
                    return calculateItemAmount(groupid, period, "270201");

                case "ASSET_CURRENT_OTHER":
                    // 其他流动资产
                    return calculateItemAmount(groupid, period, "140101");

                case "ASSET_NON_CURRENT_OTHER":
                    // 其他非流动资产
                    return calculateItemAmount(groupid, period, "190101");
                default:
                    // 如果是直接的会计科目代码，直接查询
                    if (itemCode.matches("\\d+")) {
                        return calculateItemAmount(groupid, period, itemCode);
                    }
                    log.warn("未知的科目代码: {}", itemCode);
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            log.error("计算科目金额失败: groupid={}, period={}, itemCode={}", groupid, period, itemCode, e);
            return BigDecimal.ZERO;
        }
    }

}

