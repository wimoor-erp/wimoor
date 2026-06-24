package com.wimoor.finance.report.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 报表映射规则对象 fin_report_mapping_rules
 *
 * @author wimoor
 * @date 2025-06-08
 */
@Data
public class FinReportMappingRules extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long ruleId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 模板类型：BALANCE_SHEET-资产负债表, INCOME_STATEMENT-利润表, CASH_FLOW-现金流量表 */
    @Excel(name = "模板类型")
    private String templateType;

    /** 规则类型：SUBJECT-科目映射, ITEM_SUM-项目汇总, ITEM_CONDITIONAL-条件汇总 */
    @Excel(name = "规则类型")
    private String ruleType;

    /** 报表项目编码 */
    @Excel(name = "报表项目编码")
    private String itemCode;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 匹配类型：PREFIX-前缀匹配, EXACT-精确匹配, RANGE-范围匹配, PARENT-父级科目匹配 */
    @Excel(name = "匹配类型")
    private String matchType;

    /** 匹配值（前缀/科目编码/范围起始值） */
    @Excel(name = "匹配值")
    private String matchValue;

    /** 匹配结束值（用于范围匹配） */
    @Excel(name = "匹配结束值")
    private String matchValueEnd;

    /** 操作符：ADD-加, SUBTRACT-减, EXCLUDE-排除 */
    @Excel(name = "操作符")
    private String operator;

    /** 科目类型：1-资产, 2-负债, 3-所有者权益, 4-成本, 5-损益 */
    @Excel(name = "科目类型")
    private Integer subjectType;

    /** 余额方向：1-借方, 2-贷方 */
    @Excel(name = "余额方向")
    private Integer direction;

    /** 是否仅匹配末级科目：0-否, 1-是 */
    @Excel(name = "是否仅匹配末级科目")
    private Integer isLeafOnly;

    /** 优先级（数值越小优先级越高） */
    @Excel(name = "优先级")
    private Integer priority;

    /** 状态：0-停用, 1-启用 */
    @Excel(name = "状态")
    private Integer status;

    /** 规则描述 */
    @Excel(name = "规则描述")
    private String description;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;
}
