package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.*;
import com.wimoor.finance.voucher.domain.FinVouchers;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.wimoor.common.GeneralUtil.formatDate;

@Service
public class EnhancedVoucherContinuityService {

    /**
     * 增强的凭证连续性检查
     */
    public void checkEnhancedVoucherContinuity(List<FinVouchers> vouchers , PreClosingCheckResult result) {
        ClosingCheckItem checkItem = new ClosingCheckItem();
        checkItem.setCheckItem("增强凭证连续性检查");

        try {
            if (vouchers.isEmpty()) {
                checkItem.setPassed(true);
                checkItem.setMessage("无凭证数据");
                result.addCheckItem(checkItem);
                return;
            }

            // 多种连续性检查
            List<ContinuityIssue> issues = new ArrayList<>();
            issues.addAll(checkDuplicateSequences(vouchers));
            issues.addAll(checkVoucherNoFormat(vouchers));
            issues.addAll(checkNumberGaps(vouchers));        // 编号间隙（仅作为信息）
            if (issues.isEmpty()) {
                checkItem.setPassed(true);
                checkItem.setMessage("所有连续性检查通过");
            } else {
                checkItem.setPassed(false);
                checkItem.setMessage(String.format("发现%d个连续性問題", issues.size()));
                checkItem.addRelatedData("issues", issues);

                // 根据问题严重程度设置检查级别
                boolean hasError = issues.stream()
                        .anyMatch(issue -> issue.getSeverity() == IssueSeverity.ERROR);
                checkItem.setCheckLevel(hasError ? CheckLevel.ERROR : CheckLevel.WARNING);
            }

            result.addCheckItem(checkItem);

        } catch (Exception e) {
            checkItem.setPassed(false);
            checkItem.setMessage("连续性检查异常: " + e.getMessage());
            checkItem.setCheckLevel(CheckLevel.ERROR);
            result.addCheckItem(checkItem);
        }
    }

    /**
     * 检查重复序列号
     */
    private List<ContinuityIssue> checkDuplicateSequences(List<FinVouchers> vouchers) {
        List<ContinuityIssue> issues = new ArrayList<>();

        // 按类型和日期分组
        Map<String, Map<Date, List<FinVouchers>>> grouped = groupVouchersByTypeAndDate(vouchers);

        for (Map.Entry<String, Map<Date, List<FinVouchers>>> typeEntry : grouped.entrySet()) {
            for (Map.Entry<Date, List<FinVouchers>> dateEntry : typeEntry.getValue().entrySet()) {
                List<FinVouchers> dateVouchers = dateEntry.getValue();

                // 查找重复的序列号
                Map<String, List<FinVouchers>> sequenceGroups = dateVouchers.stream()
                        .collect(Collectors.groupingBy(FinVouchers::getVoucherNo));

                for (Map.Entry<String, List<FinVouchers>> seqEntry : sequenceGroups.entrySet()) {
                    if (seqEntry.getValue().size() > 1) {
                        ContinuityIssue issue = new ContinuityIssue();
                        issue.setVoucherType(typeEntry.getKey());
                        issue.setVoucherDate(dateEntry.getKey());
                        issue.setActualSequence(Integer.parseInt(seqEntry.getKey()));
                        issue.setIssueType(ContinuityIssueType.DUPLICATE_SEQUENCE);
                        issue.setSeverity(IssueSeverity.ERROR);
                        issue.setDescription(String.format("序列号%d重复%d次",
                                seqEntry.getKey(), seqEntry.getValue().size()));
                        issues.add(issue);
                    }
                }
            }
        }

        return issues;
    }

    /**
     * 检查凭证编号格式
     */
    private List<ContinuityIssue> checkVoucherNoFormat(List<FinVouchers> vouchers) {
        return vouchers.stream()
                .filter(voucher -> !isValidVoucherNoFormat(voucher.getVoucherNo()))
                .map(voucher -> {
                    ContinuityIssue issue = new ContinuityIssue();
                    issue.setVoucherType(voucher.getVoucherType());
                    issue.setVoucherDate(voucher.getVoucherDate());
                    issue.setVoucherNo(voucher.getVoucherNo());
                    issue.setIssueType(ContinuityIssueType.INVALID_FORMAT);
                    issue.setSeverity(IssueSeverity.WARNING);
                    issue.setDescription("凭证编号格式不符合规范");
                    return issue;
                })
                .collect(Collectors.toList());
    }


    /**
     * 验证凭证编号格式
     */
    private boolean isValidVoucherNoFormat(String voucherNo) {
        if (voucherNo == null || voucherNo.trim().isEmpty()) {
            return false;
        }

        // 简单的格式验证：必须包含数字
        return voucherNo.matches(".*\\d+.*");
    }

    /**
     * 按类型和日期分组
     */
    private Map<String, Map<Date, List<FinVouchers>>> groupVouchersByTypeAndDate(List<FinVouchers> vouchers) {
        return vouchers.stream()
                .collect(Collectors.groupingBy(FinVouchers::getVoucherType,
                        Collectors.groupingBy(FinVouchers::getVoucherDate)));
    }


    /**
     * 检查编号间隙 - 仅作为信息，不是错误
     */
    private List<ContinuityIssue> checkNumberGaps(List<FinVouchers> vouchers) {
        List<ContinuityIssue> issues = new ArrayList<>();

        // 按类型和日期分组
        Map<String, Map<Date, List<FinVouchers>>> grouped =
                groupVouchersByTypeAndDate(vouchers);

        for (Map.Entry<String, Map<Date, List<FinVouchers>>> typeEntry : grouped.entrySet()) {
            for (Map.Entry<Date, List<FinVouchers>> dateEntry : typeEntry.getValue().entrySet()) {
                List<FinVouchers> dateVouchers = dateEntry.getValue();
                if (dateVouchers.size() < 2) continue;

                // 排序
                dateVouchers.sort(Comparator.comparing(FinVouchers::getVoucherNo));

                int firstNumber = Integer.parseInt(dateVouchers.get(0).getVoucherNo());
                int lastNumber = Integer.parseInt(dateVouchers.get(dateVouchers.size() - 1).getVoucherNo());
                int expectedCount = lastNumber - firstNumber + 1;

                if (dateVouchers.size() < expectedCount) {
                    // 有编号间隙，但只是信息
                    ContinuityIssue issue = new ContinuityIssue();
                    issue.setVoucherType(typeEntry.getKey());
                    issue.setIssueType(ContinuityIssueType.NUMBER_GAP);
                    issue.setSeverity(IssueSeverity.INFO);
                    issue.setVoucherDate(dateEntry.getKey());
                    issue.setDescription(String.format("%s凭证在%s有编号间隙(应有%d张,实有%d张)",
                            typeEntry.getKey(), formatDate(dateEntry.getKey()), expectedCount, dateVouchers.size()));
                    issues.add(issue);
                }
            }
        }

        return issues;
    }

}