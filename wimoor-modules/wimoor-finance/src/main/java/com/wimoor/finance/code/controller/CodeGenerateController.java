package com.wimoor.finance.code.controller;

import com.wimoor.finance.code.domain.FinCodeGenerateLog;
import com.wimoor.finance.code.domain.FinCodeRule;
import com.wimoor.finance.code.domain.dto.CodeGenerateRequest;
import com.wimoor.finance.code.domain.dto.CodeGenerateResponse;
import com.wimoor.finance.code.service.CodeGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/code")
public class CodeGenerateController {

    @Autowired
    private CodeGenerateService codeGenerateService;

    @PostMapping("/generate")
    public CodeGenerateResponse generateCode(@Valid @RequestBody CodeGenerateRequest request) {
        return codeGenerateService.generateCode(request);
    }

    @GetMapping("/rules/{tenantId}")
    public List<FinCodeRule> getAllRules(@PathVariable String groupid) {
        return codeGenerateService.getAllRules(groupid);
    }

    @GetMapping("/rules/{tenantId}/{type}")
    public List<FinCodeRule> getRulesByType(@PathVariable String groupid, @PathVariable String type) {
        return codeGenerateService.getRulesByType(groupid, type);
    }

    @PostMapping("/rules")
    public FinCodeRule createRule(@RequestBody FinCodeRule codeRule) {
        return codeGenerateService.createRule(codeRule);
    }

    @PutMapping("/rules/{id}")
    public FinCodeRule updateRule(@PathVariable Long id, @RequestBody FinCodeRule codeRule) {
        codeRule.setId(id);
        return codeGenerateService.updateRule(codeRule);
    }

    @DeleteMapping("/rules/{id}")
    public void deleteRule(@PathVariable Long id) {
        codeGenerateService.deleteRule(id);
    }

    @GetMapping("/history/{tenantId}")
    public List<FinCodeGenerateLog> getGenerateHistory(
            @PathVariable Long tenantId,
            @RequestParam(required = false) String ruleCode,
            @RequestParam(required = false) String groupid) {
        return codeGenerateService.getGenerateHistory(ruleCode, groupid);
    }
}