package com.wimoor.finance.ledger.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubjectBalanceQueryDTO {
    /**
     * 集团ID
     */
    private String groupid;
    
    /**
     * 开始会计期间 (YYYYMM格式)
     */
    private String beginPeriod;
    
    /**
     * 结束会计期间 (YYYYMM格式)
     */
    private String endPeriod;
    
    /**
     * 会计期间 (YYYYMM格式，兼容旧版单期间查询)
     */
    private String period;
    
    /**
     * 科目编码（支持模糊查询）
     */
    private String subjectCode;
    
    /**
     * 科目名称（支持模糊查询）
     */
    private String subjectName;
    
    /**
     * 科目类型
     */
    private Integer subjectType;
    
    /**
     * 是否只查询末级科目
     */
    private Boolean leafOnly;
    
    /**
     * 科目编码列表（批量查询）
     */
    private List<String> subjectCodes;
    
    /**
     * 层级过滤
     */
    private Integer level;
    
    /**
     * 上级科目编码
     */
    private String parentCode;

    private Boolean isFilterZero;
}