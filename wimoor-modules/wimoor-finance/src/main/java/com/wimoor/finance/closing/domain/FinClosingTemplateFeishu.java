package com.wimoor.finance.closing.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 结算飞书格同步对象 fin_closing_template_feishu
 * 
 * @author wimoor
 * @date 2026-05-11
 */
public class FinClosingTemplateFeishu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 结算模版ID */
    private String templateid;

    /** Admin模块飞书表格ID */
    @Excel(name = "Admin模块飞书表格ID")
    private String feishuTableId;

    /** 飞书查询过滤 */
    @Excel(name = "飞书查询过滤")
    private String filter;

    /** 摘要字段 */
    @Excel(name = "摘要字段")
    private String summaryField;

    /** 会计时间字段 */
    @Excel(name = "会计时间字段")
    private String voucherDateField;

    /** 会计科目字段 */
    @Excel(name = "会计科目字段")
    private String subjectField;

    /** 金额字段 */
    @Excel(name = "金额字段")
    private String amountField;

    /** 会计日期汇总类型，0按日，1按月，2单笔生成凭证 */
    @Excel(name = "会计日期汇总类型，0按日，1按月，2单笔生成凭证")
    private Integer datetype;

    public void setTemplateid(String templateid) 
    {
        this.templateid = templateid;
    }

    public String getTemplateid() 
    {
        return templateid;
    }

    public void setFeishuTableId(String feishuTableId) 
    {
        this.feishuTableId = feishuTableId;
    }

    public String getFeishuTableId() 
    {
        return feishuTableId;
    }

    public void setFilter(String filter) 
    {
        this.filter = filter;
    }

    public String getFilter() 
    {
        return filter;
    }

    public void setSummaryField(String summaryField) 
    {
        this.summaryField = summaryField;
    }

    public String getSummaryField() 
    {
        return summaryField;
    }

    public void setVoucherDateField(String voucherDateField) 
    {
        this.voucherDateField = voucherDateField;
    }

    public String getVoucherDateField() 
    {
        return voucherDateField;
    }

    public void setSubjectField(String subjectField) 
    {
        this.subjectField = subjectField;
    }

    public String getSubjectField() 
    {
        return subjectField;
    }

    public void setDatetype(Integer datetype) 
    {
        this.datetype = datetype;
    }

    public Integer getDatetype() 
    {
        return datetype;
    }

    /** 金额字段 */
    public void setAmountField(String amountField)
        {
            this.amountField = amountField;
        }

    /** 金额字段 */
    public String getAmountField()
        {
            return amountField;
        }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateid", getTemplateid())
            .append("feishuTableId", getFeishuTableId())
            .append("filter", getFilter())
            .append("summaryField", getSummaryField())
            .append("voucherDateField", getVoucherDateField())
            .append("subjectField", getSubjectField())
            .append("datetype", getDatetype())
            .toString();
    }
}
