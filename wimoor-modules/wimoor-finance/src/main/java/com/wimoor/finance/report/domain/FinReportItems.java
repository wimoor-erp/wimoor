package com.wimoor.finance.report.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 报表项目对象 fin_report_items
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Data
public class FinReportItems extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报表项目ID */
    private Long itemId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 关联的报表模板ID */
    @Excel(name = "关联的报表模板ID")
    private Long templateId;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;


    /** 项目级别：1-一级项目，2-二级项目 */
    @Excel(name = "项目级别")
    private Integer itemLevel;

    @Excel(name = "行次")
    private Integer lineNumber;
    /** 父级项目ID */
    @Excel(name = "父级项目编码")
    private String parentCode;

    /** 公式类型：1-直接取值，2-计算
     * 公式类型：DIRECT-直接取值, FORMULA-公式计算, CUSTOM-自定义, CALCULATED-自动计算
     * */
    @Excel(name = "公式类型")
    private String formulaType;

    /** 公式内容 */
    @Excel(name = "公式内容")
    private String formulaContent;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String itemType;
    /** 项目分类 */
    @Excel(name = "项目分类")
    private String itemCategory;
    /** 公式内容 */
    @Excel(name = "计算规则")
    private String calculationRule;
    /** 数据来源：SUBJECT-会计科目, CUSTOM-自定义, CALCULATED-计算, CONSTANT-常量 */
    @Excel(name = "数据来源")
    private String dataSource;
    /** 关联科目编码（多个用逗号分隔） */
    @Excel(name = "关联科目编码")
    private String subjectCodes;

    /** 金额类型：END_BALANCE-期末余额, BEGIN_BALANCE-期初余额, DEBIT_TOTAL-借方发生额, CREDIT_TOTAL-贷方发生额 */
    @Excel(name = "金额类型")
    private String amountType;

    /** 金额方向：1-正数, -1-负数, NULL-自动判断 */
    @Excel(name = "金额方向")
    private String direction;

    /** 显示格式：NORMAL-正常, BOLD-加粗, ITALIC-斜体, UNDERLINE-下划线 */
    @Excel(name = "显示格式")
    private String displayFormat;

    /** 是否显示零值：0-不显示, 1-显示 */
    @Excel(name = "是否显示零值")
    private Boolean isShowZero;

    /** 是否显示：0-不显示, 1-显示 */
    @Excel(name = "是否显示")
    private Boolean isShow;

     /** 是否末级项目：0-否, 1-是 */
    @Excel(name = "是否末级项目")
    private Boolean isLeaf;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortOrder;

    /** 状态：0-停用, 1-启用 */
    @Excel(name = "状态")
    private Integer status;
     /** 项目描述 */
    @Excel(name = "项目描述")
    private String description;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    @Excel(name = "创建人")
    private String createdBy;
}
