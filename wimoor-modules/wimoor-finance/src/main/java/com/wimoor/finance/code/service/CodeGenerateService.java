package com.wimoor.finance.code.service;

import com.wimoor.finance.code.domain.FinCodeGenerateLog;
import com.wimoor.finance.code.domain.FinCodeRule;
import com.wimoor.finance.code.domain.dto.CodeGenerateRequest;
import com.wimoor.finance.code.domain.dto.CodeGenerateResponse;

import java.util.List;

public interface CodeGenerateService {

    CodeGenerateResponse generateCode(CodeGenerateRequest request);

    List<FinCodeRule> getAllRules(String groupid);

    List<FinCodeRule> getRulesByType(String groupid, String ruleType);

    FinCodeRule createRule(FinCodeRule codeRule);

    FinCodeRule updateRule(FinCodeRule codeRule);

    void deleteRule(Long id);

    List<FinCodeGenerateLog> getGenerateHistory(String ruleCode, String groupid);
}