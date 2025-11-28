package com.wimoor.finance.code.service.impl;


import com.wimoor.finance.code.domain.FinCodeGenerateLog;
import com.wimoor.finance.code.domain.FinCodeRule;
import com.wimoor.finance.code.domain.dto.CodeGenerateRequest;
import com.wimoor.finance.code.domain.dto.CodeGenerateResponse;
import com.wimoor.finance.code.service.CodeGenerateService;
import com.wimoor.finance.code.service.IFinCodeGenerateLogService;
import com.wimoor.finance.code.service.IFinCodeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CodeGenerateServiceImpl implements CodeGenerateService {

    @Autowired
    private IFinCodeRuleService iFinCodeRuleService;

    @Autowired
    private IFinCodeGenerateLogService iFinCodeGenerateLogService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public CodeGenerateResponse generateCode(CodeGenerateRequest request) {
        try {
            Optional<FinCodeRule> ruleOpt = iFinCodeRuleService.findByRuleCode(request.getRuleCode(),request.getGroupid());
            if (!ruleOpt.isPresent()) {
                return new CodeGenerateResponse(false, null, "编码规则不存在");
            }

            FinCodeRule rule = ruleOpt.get();
            if (rule.getStatus() != 1) {
                return new CodeGenerateResponse(false, null, "编码规则已停用");
            }

            String generatedCode = generateCodeByRule(rule, request);

            // 检查重复
            String groupid = request.getGroupid();
            if (iFinCodeGenerateLogService.existsByGeneratedCode(groupid,generatedCode)) {
                return new CodeGenerateResponse(false, null, "生成的编码已存在");
            }

            // 保存生成记录
            FinCodeGenerateLog log = new FinCodeGenerateLog();
            log.setGeneratedCode(generatedCode);
            log.setRuleCode(rule.getRuleCode());
            log.setBusinessType(request.getBusinessType());
            log.setBusinessName(request.getBusinessName());
            log.setAccountSetId(request.getAccountSetId());
            log.setOperator("system"); // 实际应从上下文中获取
            iFinCodeGenerateLogService.insertFinCodeGenerateLog(log);

            // 更新当前值（如果需要）
            if (rule.getAutoIncrement()) {
                rule.setCurrentValue(rule.getCurrentValue() + 1);
                rule.setUpdatedTime(new Date());
                iFinCodeRuleService.insertFinCodeRule(rule);
            }

            CodeGenerateResponse response = new CodeGenerateResponse(true, generatedCode, "编码生成成功");
            response.setRuleName(rule.getRuleName());
            return response;

        } catch (Exception e) {
            return new CodeGenerateResponse(false, null, "编码生成失败: " + e.getMessage());
        }
    }

    private String generateCodeByRule(FinCodeRule rule, CodeGenerateRequest request) {
        String ruleType = rule.getRuleType();
        String structure = rule.getRuleStructure();
        String separator = rule.getSeparator();

        switch (ruleType) {
            case "SUBJECT":
                return generateSubjectCode(rule, request, structure, separator);
            case "VOUCHER":
                return generateVoucherCode(rule, request, structure, separator);
            case "AUXILIARY":
                return generateAuxiliaryCode(rule, request, structure, separator);
            case "ASSET":
                return generateAssetCode(rule, request, structure, separator);
            case "REPORT":
                return generateReportCode(rule, request, structure, separator);
            default:
                throw new IllegalArgumentException("不支持的编码规则类型: " + ruleType);
        }
    }

    private String generateSubjectCode(FinCodeRule rule, CodeGenerateRequest request, String structure, String separator) {
        if (request.getParentCode() != null && !request.getParentCode().isEmpty()) {
            // 生成子科目编码
            String groupid = request.getGroupid();
            return generateChildSubjectCode(groupid,request.getParentCode(), structure, separator);
        } else {
            // 生成一级科目编码
            return generateFirstLevelSubjectCode(request, structure);
        }
    }

    private String generateChildSubjectCode(String groupid,String parentCode, String structure, String separator) {
        // 查找同级科目的最大编码
        String prefix = parentCode;
        int sequenceLength = getSequenceLength(structure, parentCode.length());

        Long maxSeq = iFinCodeGenerateLogService.findMaxSequence(groupid,"SUBJECT", prefix,
                parentCode.length() + 1, sequenceLength);

        long nextSeq = (maxSeq != null ? maxSeq + 1 : 1);
        String sequence = String.format("%0" + sequenceLength + "d", nextSeq);

        if (separator != null && !separator.isEmpty()) {
            return parentCode + separator + sequence;
        } else {
            return parentCode + sequence;
        }
    }

    private String generateFirstLevelSubjectCode(CodeGenerateRequest request, String structure) {
        String[] parts = structure.split("-");
        int firstLevelLength = Integer.parseInt(parts[0]);

        // 根据业务类型确定科目类型前缀
        String typePrefix = getSubjectTypePrefix(request.getBusinessType());

        // 查找该类型下的最大编码
        String groupid = request.getGroupid();
        Long maxSeq = iFinCodeGenerateLogService.findMaxSequence(groupid,"SUBJECT", typePrefix,
                typePrefix.length(), firstLevelLength - typePrefix.length());

        long nextSeq = (maxSeq != null ? maxSeq + 1 : 1);
        String sequence = String.format("%0" + (firstLevelLength - typePrefix.length()) + "d", nextSeq);

        return typePrefix + sequence;
    }

    private String getSubjectTypePrefix(String businessType) {
        switch (businessType) {
            case "ASSET": return "1";
            case "LIABILITY": return "2";
            case "EQUITY": return "3";
            case "COST": return "4";
            case "INCOME": return "5";
            default: return "1";
        }
    }

    private String generateVoucherCode(FinCodeRule rule, CodeGenerateRequest request, String structure, String separator) {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        // 查找当天的最大凭证号
        String groupid = request.getGroupid();
        Long maxSeq = iFinCodeGenerateLogService.findMaxSequence(groupid,"VOUCHER", datePart,
                datePart.length(), 5); // 5位序列号

        long nextSeq = (maxSeq != null ? maxSeq + 1 : 1);
        String sequence = String.format("%05d", nextSeq);

        return "V" + datePart + sequence;
    }

    private String generateAuxiliaryCode(FinCodeRule rule, CodeGenerateRequest request, String structure, String separator) {
        String typePrefix = getAuxiliaryTypePrefix(request.getBusinessType());

        String groupid = request.getGroupid();
        Long maxSeq = iFinCodeGenerateLogService.findMaxSequence(groupid,"AUXILIARY", typePrefix,
                typePrefix.length(), 4);

        long nextSeq = (maxSeq != null ? maxSeq + 1 : 1);
        String sequence = String.format("%04d", nextSeq);

        return typePrefix + sequence;
    }

    private String getAuxiliaryTypePrefix(String businessType) {
        switch (businessType) {
            case "DEPARTMENT": return "DEPT";
            case "EMPLOYEE": return "EMP";
            case "CUSTOMER": return "CUST";
            case "SUPPLIER": return "SUPP";
            case "PROJECT": return "PROJ";
            default: return "AUX";
        }
    }

    private String generateAssetCode(FinCodeRule rule, CodeGenerateRequest request, String structure, String separator) {
        String datePart = LocalDateTime.now().format(MONTH_FORMATTER);

        String groupid = request.getGroupid();
        Long maxSeq = iFinCodeGenerateLogService.findMaxSequence(groupid,"ASSET", "FA" + datePart,
                10, 4); // FA + yyyyMM + 4位序列号

        long nextSeq = (maxSeq != null ? maxSeq + 1 : 1);
        String sequence = String.format("%04d", nextSeq);

        return "FA" + datePart + sequence;
    }

    private String generateReportCode(FinCodeRule rule, CodeGenerateRequest request, String structure, String separator) {
        String reportType = request.getBusinessType();
        String lineNumber = request.getCustomParams(); // 假设自定义参数中包含行次

        return reportType + "-" + lineNumber;
    }

    private int getSequenceLength(String structure, int parentLength) {
        String[] parts = structure.split("-");
        for (String part : parts) {
            int length = Integer.parseInt(part);
            if (length > parentLength) {
                return length - parentLength;
            }
        }
        return 2; // 默认2位
    }

     @Override
    public List<FinCodeRule> getAllRules(String groupid) {
        return iFinCodeRuleService.findByStatus(1L, groupid);
    }

    @Override
    public List<FinCodeRule> getRulesByType(String groupid, String ruleType) {
        return iFinCodeRuleService.findByTenantIdAndRuleTypeAndStatus(groupid, ruleType, 1L);
    }

    @Override
    public FinCodeRule createRule(FinCodeRule codeRule) {
           iFinCodeRuleService.insertFinCodeRule(codeRule);
           return codeRule;
    }

    @Override
    public FinCodeRule updateRule(FinCodeRule codeRule) {
        codeRule.setUpdatedTime(new Date());
        iFinCodeRuleService.updateFinCodeRule(codeRule);
        return codeRule;
    }

    @Override
    public void deleteRule(Long id) {
        iFinCodeRuleService.deleteFinCodeRuleById(id);
    }

    @Override
    public List<FinCodeGenerateLog> getGenerateHistory(String ruleCode, String groupid) {
        if (ruleCode != null) {
            return iFinCodeGenerateLogService.findByRuleCodeOrderByGeneratedTimeDesc(groupid,ruleCode);
        } else  {
            return iFinCodeGenerateLogService.findByRuleCodeOrderByGeneratedTimeDesc(groupid,null);
        }
    }
}