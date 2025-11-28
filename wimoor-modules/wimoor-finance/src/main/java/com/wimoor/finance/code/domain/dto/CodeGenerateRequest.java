package com.wimoor.finance.code.domain.dto;

import javax.validation.constraints.NotBlank;

public class CodeGenerateRequest {

    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    private String businessType;

    private String businessName;

    private Long accountSetId;

    private String parentCode;

    private  String groupid ;

    private String customParams; // JSON格式的自定义参数

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    // Getters and Setters
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public Long getAccountSetId() { return accountSetId; }
    public void setAccountSetId(Long accountSetId) { this.accountSetId = accountSetId; }

    public String getParentCode() { return parentCode; }
    public void setParentCode(String parentCode) { this.parentCode = parentCode; }

    public String getCustomParams() { return customParams; }
    public void setCustomParams(String customParams) { this.customParams = customParams; }
}