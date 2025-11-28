package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ClosingResult {
    private boolean success;
    private String message;
    private String period;
    private Date closingTime;
    private ClosingReport report;
    private List<String> warnings = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public static ClosingResult success(String message, ClosingReport report) {
        ClosingResult result = new ClosingResult();
        result.setSuccess(true);
        result.setMessage(message);
        result.setReport(report);
        result.setClosingTime(new Date());
        return result;
    }

    public static ClosingResult fail(String message) {
        ClosingResult result = new ClosingResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public void addError(String error) {
        this.errors.add(error);
    }
}