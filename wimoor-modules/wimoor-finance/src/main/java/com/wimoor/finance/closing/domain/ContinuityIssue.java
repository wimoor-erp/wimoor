package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ContinuityIssue {

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 凭证日期
     */
    private Date voucherDate;

    /**
     * 凭证编号
     */
    private String voucherNo;

    /**
     * 期望序列号
     */
    private Integer expectedSequence;

    /**
     * 实际序列号
     */
    private Integer actualSequence;

    /**
     * 问题类型
     */
    private ContinuityIssueType issueType;

    /**
     * 缺失的序列号列表
     */
    private List<Integer> missingSequences = new ArrayList<>();

    /**
     * 问题描述
     */
    private String description;

    /**
     * 严重程度
     */
    private IssueSeverity severity = IssueSeverity.WARNING;

    /**
     * 获取问题详情
     */
    public String getIssueDetail() {
        switch (issueType) {
            case MISSING_SEQUENCE:
                String missingStr = missingSequences.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                return String.format("期望编号%d, 实际编号%d, 缺失编号:%s",
                        expectedSequence, actualSequence, missingStr);
            case DUPLICATE_SEQUENCE:
                return String.format("编号重复: %d", actualSequence);
            case INVALID_FORMAT:
                return "凭证编号格式无效";
            default:
                return "未知连续性問題";
        }
    }
}