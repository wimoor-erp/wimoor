package com.wimoor.finance.ledger.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 科目余额表数据传输对象
 */
@Data
public class SubjectBalanceTableDTO {
    /**
     * 科目编码
     */
    private String subjectCode;
    
    /**
     * 科目名称
     */
    private String subjectName;
    
    /**
     * 科目类型
     */
    private Integer subjectType;
    
    /**
     * 科目类型名称
     */
    private String subjectTypeName;
    
    /**
     * 科目方向（1-借方，2-贷方）
     */
    private Integer direction;
    
    /**
     * 层级
     */
    private Integer level;
    
    /**
     * 科目级别（前端显示用）
     */
    private Integer subjectLevel;
    
    /**
     * 上级科目编码
     */
    private String parentCode;
    
    /**
     * 是否末级
     */
    private Boolean isLeaf;
    
    /**
     * 期初余额（借方）
     */
    private BigDecimal beginDebit;
    
    /**
     * 期初余额（贷方）
     */
    private BigDecimal beginCredit;
    
    /**
     * 期初余额（统一值）
     */
    private BigDecimal beginBalance;
    
    /**
     * 期初余额方向（1-借方，2-贷方）
     */
    private Integer beginDirection;
    
    /**
     * 本期借方发生额
     */
    private BigDecimal debitAmount;
    
    /**
     * 本期贷方发生额
     */
    private BigDecimal creditAmount;
    
    /**
     * 期末余额（借方）
     */
    private BigDecimal endDebit;
    
    /**
     * 期末余额（贷方）
     */
    private BigDecimal endCredit;
    
    /**
     * 期末余额（统一值）
     */
    private BigDecimal endBalance;
    
    /**
     * 期末余额方向（1-借方，2-贷方）
     */
    private Integer endDirection;
    
    /**
     * 本年累计借方
     */
    private BigDecimal yearDebitAmount;
    
    /**
     * 本年累计贷方
     */
    private BigDecimal yearCreditAmount;
    
    /**
     * 子科目列表（用于树形展示）
     */
    private List<SubjectBalanceTableDTO> children;
    
    /**
     * 是否有子节点（树形展示用）
     */
    private Boolean hasChildren;
    
    /**
     * 格式化显示科目编码和名称
     */
    public String getDisplayName() {
        return subjectCode + " " + subjectName;
    }
    
    /**
     * 判断是否有余额
     */
    public boolean hasBalance() {
        return (beginDebit != null && beginDebit.compareTo(BigDecimal.ZERO) != 0) ||
               (beginCredit != null && beginCredit.compareTo(BigDecimal.ZERO) != 0) ||
               (debitAmount != null && debitAmount.compareTo(BigDecimal.ZERO) != 0) ||
               (creditAmount != null && creditAmount.compareTo(BigDecimal.ZERO) != 0) ||
               (endDebit != null && endDebit.compareTo(BigDecimal.ZERO) != 0) ||
               (endCredit != null && endCredit.compareTo(BigDecimal.ZERO) != 0);
    }
}