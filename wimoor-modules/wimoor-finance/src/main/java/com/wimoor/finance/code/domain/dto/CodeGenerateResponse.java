package com.wimoor.finance.code.domain.dto;

public class CodeGenerateResponse {
    private boolean success;
    private String generatedCode;
    private String message;
    private String ruleName;

    private  Long tenantId ;

    // Constructors
    public CodeGenerateResponse() {}

    public CodeGenerateResponse(boolean success, String generatedCode, String message) {
        this.success = success;
        this.generatedCode = generatedCode;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getGeneratedCode() { return generatedCode; }
    public void setGeneratedCode(String generatedCode) { this.generatedCode = generatedCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
}