package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferError {

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 错误时间
     */
    private Date errorTime = new Date();

    /**
     * 错误类型
     */
    private ErrorType errorType = ErrorType.UNKNOWN;

    /**
     * 错误详情
     */
    private Map<String, Object> errorDetails = new HashMap<>();

    // 构造方法
    public TransferError(Long subjectId, String subjectName, String errorMessage, ErrorType errorType) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    /**
     * 添加错误详情
     */
    public void addErrorDetail(String key, Object value) {
        this.errorDetails.put(key, value);
    }

    /**
     * 获取完整的错误描述
     */
    public String getFullErrorMessage() {
        return String.format("科目[%s](ID:%d)结转失败: %s [%s]",
                subjectName, subjectId, errorMessage, errorType.getDescription());
    }
}
