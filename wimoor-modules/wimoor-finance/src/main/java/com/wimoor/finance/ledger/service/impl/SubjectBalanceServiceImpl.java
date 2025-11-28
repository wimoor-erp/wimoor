package com.wimoor.finance.ledger.service.impl;

import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceDTO;
import com.wimoor.finance.ledger.mapper.FinDetailLedgerMapper;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.ledger.service.ISubjectBalanceService;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.mapper.FinAccountingSubjectsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectBalanceServiceImpl implements ISubjectBalanceService {

    @Autowired
    private FinDetailLedgerMapper finDetailLedgerMapper;

    @Autowired
    IFinGeneralLedgerService finGeneralLedgerService;

    @Autowired
    private FinAccountingSubjectsMapper finAccountingSubjectsMapper;

    private static final DateTimeFormatter PERIOD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public BigDecimal getSubjectBalance(String groupid, String period, String subjectCodes, String amountType) {
        try {
            // 解析期间
            LocalDate[] dateRange = parsePeriod(period);
            LocalDate startDate = dateRange[0];
            LocalDate endDate = dateRange[1];

            // 解析科目编码（支持多个科目用逗号分隔）
            List<String> subjectCodeList = parseSubjectCodes(subjectCodes);

            if (subjectCodeList.isEmpty()) {
                return BigDecimal.ZERO;
            }

            // 如果是单个科目编码且不包含通配符
            if (subjectCodeList.size() == 1 && !subjectCodeList.get(0).contains("%")) {
                return calculateSingleSubjectBalance(groupid, subjectCodeList.get(0), startDate, endDate, amountType);
            } else {
                // 多个科目或包含通配符的科目
                return calculateMultipleSubjectsBalance(groupid, subjectCodeList, startDate, endDate, amountType);
            }

        } catch (Exception e) {
            throw new RuntimeException("获取科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, BigDecimal> getSubjectBalances(String groupid, String period, List<String> subjectCodes, String amountType) {
        Map<String, BigDecimal> result = new HashMap<>();

        try {
            LocalDate[] dateRange = parsePeriod(period);
            LocalDate startDate = dateRange[0];
            LocalDate endDate = dateRange[1];

            // 批量查询科目余额
            List<SubjectBalanceDTO> balanceDTOs = finDetailLedgerMapper.selectSubjectBalances(groupid, subjectCodes, startDate, endDate);

            for (SubjectBalanceDTO dto : balanceDTOs) {
                BigDecimal amount = getAmountByType(dto, amountType);
                result.put(dto.getSubjectCode(), amount);
            }

            // 处理未找到的科目
            for (String subjectCode : subjectCodes) {
                if (!result.containsKey(subjectCode)) {
                    result.put(subjectCode, BigDecimal.ZERO);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("批量获取科目余额失败: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<FinDetailLedger> getSubjectDetails(String groupid, String period, String subjectCode) {
        LocalDate[] dateRange = parsePeriod(period);
        return finDetailLedgerMapper.selectBySubjectAndPeriod(groupid, subjectCode, dateRange[0], dateRange[1]);
    }

    @Override
    public SubjectBalanceDTO getSubjectBalanceDetail(String groupid, String period, String subjectCode) {
        try {
            LocalDate[] dateRange = parsePeriod(period);
            LocalDate startDate = dateRange[0];
            LocalDate endDate = dateRange[1];

            // 获取科目信息
            FinAccountingSubjects subject = finAccountingSubjectsMapper.selectByTenantIdAndSubjectCode(groupid, subjectCode);
            if (subject == null) {
                throw new RuntimeException("科目不存在: " + subjectCode);
            }

            // 获取余额信息
            List<SubjectBalanceDTO> balanceDTOs = finDetailLedgerMapper.selectSubjectBalances(
                    groupid, Collections.singletonList(subjectCode), startDate, endDate);

            if (!balanceDTOs.isEmpty()) {
                return balanceDTOs.get(0);
            } else {
                // 如果没有明细记录，返回空余额信息
                SubjectBalanceDTO dto = new SubjectBalanceDTO();
                dto.setSubjectCode(subjectCode);
                dto.setSubjectName(subject.getSubjectName());
                dto.setSubjectType(subject.getSubjectType());
                dto.setDirection(subject.getDirection());
                dto.setBeginBalance(BigDecimal.ZERO);
                dto.setBeginDirection(subject.getDirection());
                dto.setDebitTotal(BigDecimal.ZERO);
                dto.setCreditTotal(BigDecimal.ZERO);
                dto.setEndBalance(BigDecimal.ZERO);
                dto.setEndDirection(subject.getDirection());
                return dto;
            }

        } catch (Exception e) {
            throw new RuntimeException("获取科目余额详情失败: " + e.getMessage(), e);
        }
    }

    private BigDecimal calculateSingleSubjectBalance(String groupid, String subjectCode,
                                                   LocalDate startDate, LocalDate endDate,
                                                   String amountType) {
        // 获取科目信息
        FinAccountingSubjects subject = finAccountingSubjectsMapper.selectByTenantIdAndSubjectCode(groupid, subjectCode);
        if (subject == null) {
            return BigDecimal.ZERO;
        }

        switch (amountType) {
            case "BEGIN_BALANCE":
                return getBeginBalance(groupid, subjectCode, startDate, subject.getDirection());

            case "END_BALANCE":
                return getEndBalance(groupid, subjectCode, endDate, subject.getDirection());

            case "DEBIT_TOTAL":
                return finDetailLedgerMapper.sumDebitAmountBySubjectAndPeriod(groupid, subjectCode, startDate, endDate);

            case "CREDIT_TOTAL":
                return finDetailLedgerMapper.sumCreditAmountBySubjectAndPeriod(groupid, subjectCode, startDate, endDate);

            case "NET_AMOUNT":
                BigDecimal debitTotal = finDetailLedgerMapper.sumDebitAmountBySubjectAndPeriod(groupid, subjectCode, startDate, endDate);
                BigDecimal creditTotal = finDetailLedgerMapper.sumCreditAmountBySubjectAndPeriod(groupid, subjectCode, startDate, endDate);
                if (subject.getDirection() == 1) { // 借方科目
                    return debitTotal.subtract(creditTotal);
                } else { // 贷方科目
                    return creditTotal.subtract(debitTotal);
                }

            default:
                throw new IllegalArgumentException("不支持的金额类型: " + amountType);
        }
    }

    private BigDecimal calculateMultipleSubjectsBalance(String groupid, List<String> subjectCodes,
                                                       LocalDate startDate, LocalDate endDate,
                                                       String amountType) {
        BigDecimal total = BigDecimal.ZERO;

        for (String subjectCode : subjectCodes) {
            if (subjectCode.contains("%")) {
                // 处理通配符科目编码
                BigDecimal amount = finDetailLedgerMapper.calculateSubjectGroupBalance(
                    groupid, subjectCode, startDate, endDate, amountType);
                total = total.add(amount);
            } else {
                // 单个科目
                BigDecimal amount = calculateSingleSubjectBalance(groupid, subjectCode, startDate, endDate, amountType);
                total = total.add(amount);
            }
        }

        return total;
    }

    private BigDecimal getBeginBalance(String groupid, String subjectCode, LocalDate startDate, Integer defaultDirection) {
        List<FinDetailLedger> latestRecords = finDetailLedgerMapper.selectLatestBalanceBeforeDate(groupid, subjectCode, startDate);
        if (!latestRecords.isEmpty()) {
            FinDetailLedger latest = latestRecords.get(0);
            return adjustBalanceByDirection(latest.getBalance(), latest.getBalanceDirection());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getEndBalance(String groupid, String subjectCode, LocalDate endDate, Integer defaultDirection) {
        List<FinDetailLedger> latestRecords = finDetailLedgerMapper.selectLatestBalanceByDate(groupid, subjectCode, endDate);
        if (!latestRecords.isEmpty()) {
            FinDetailLedger latest = latestRecords.get(0);
            return adjustBalanceByDirection(latest.getBalance(), latest.getBalanceDirection());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal adjustBalanceByDirection(BigDecimal balance, Integer direction) {
        if (balance == null) {
            return BigDecimal.ZERO;
        }
        // 统一转换为正数表示，方向在计算时处理
        return balance.abs();
    }

    public  Map<String,BigDecimal> getAllSubjectBalance(String groupid,String period) {
        Map<String,BigDecimal> result = new HashMap<>();
        List<Map<String, Object>> list = finGeneralLedgerService.getAllSubjectBalance(groupid, period);
        for(Map<String, Object> map : list) {
            result.put("ACC_" + map.get("subjectCode").toString(), map.get("endBalance")!=null?new BigDecimal(map.get("endBalance").toString()):BigDecimal.ZERO);
        }
        return result;
    }
    private BigDecimal getAmountByType(SubjectBalanceDTO dto, String amountType) {
        switch (amountType) {
            case "BEGIN_BALANCE":
                return adjustBalanceByDirection(dto.getBeginBalance(), dto.getBeginDirection());

            case "END_BALANCE":
                return adjustBalanceByDirection(dto.getEndBalance(), dto.getEndDirection());

            case "DEBIT_TOTAL":
                return dto.getDebitTotal();

            case "CREDIT_TOTAL":
                return dto.getCreditTotal();

            case "NET_AMOUNT":
                if (dto.getDirection() == 1) { // 借方科目
                    return dto.getDebitTotal().subtract(dto.getCreditTotal());
                } else { // 贷方科目
                    return dto.getCreditTotal().subtract(dto.getDebitTotal());
                }

            default:
                throw new IllegalArgumentException("不支持的金额类型: " + amountType);
        }
    }

    private LocalDate[] parsePeriod(String period) {
        if (period.length() != 6) {
            throw new IllegalArgumentException("期间格式错误，应为YYYYMM: " + period);
        }

        int year = Integer.parseInt(period.substring(0, 4));
        int month = Integer.parseInt(period.substring(4, 6));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return new LocalDate[]{startDate, endDate};
    }

    private List<String> parseSubjectCodes(String subjectCodes) {
        if (subjectCodes == null || subjectCodes.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(subjectCodes.split(","))
                .map(String::trim)
                .filter(code -> !code.isEmpty())
                .collect(Collectors.toList());
    }
}