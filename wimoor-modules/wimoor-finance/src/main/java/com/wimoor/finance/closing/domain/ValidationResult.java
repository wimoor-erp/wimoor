package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {

    /**
     * 是否有效
     */
    private Boolean valid = true;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 警告信息列表
     */
    private List<String> warnings = new ArrayList<>();

    /**
     * 验证时间
     */
    private Date validateTime = new Date();

    // 构造方法
    public ValidationResult(Boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public ValidationResult(Boolean valid, String errorMessage, List<String> warnings) {
        this.valid = valid;
        this.errorMessage = errorMessage;
        this.warnings = warnings != null ? warnings : new ArrayList<>();
    }

    // 静态工厂方法
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult fail(String errorMessage) {
        return new ValidationResult(false, errorMessage);
    }

    // 业务方法
    public void addWarning(String warning) {
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        warnings.add(warning);
    }

    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }

    public String getValidationSummary() {
        if (!valid) {
            return "验证失败: " + errorMessage;
        }

        if (hasWarnings()) {
            return "验证通过，但有警告: " + String.join("; ", warnings);
        }

        return "验证通过";
    }

    public ValidationResult copy() {
        return new ValidationResult(valid, errorMessage,
                warnings != null ? new ArrayList<>(warnings) : null, validateTime);
    }
}
