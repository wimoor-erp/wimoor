package com.wimoor.finance.closing.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 结转损益凭证模板配置对象 fin_closing_template_profit_loss
 * 
 * @author wimoor
 * @date 2026-06-10
 */
public class FinClosingTemplateProfitLoss extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 结账模板ID，关联fin_closing_template.id */
    @Excel(name = "结账模板ID，关联fin_closing_template.id")
    private String templateId;

    /** 账套ID */
    @Excel(name = "账套ID")
    private String groupid;

    /** 结转周期：0-按月结转损益，1-按年结转损益 */
    @Excel(name = "结转周期：0-按月结转损益，1-按年结转损益")
    private String transferCycle;

    /** 结转方式：0-追加结转，1-重新结转 */
    @Excel(name = "结转方式：0-追加结转，1-重新结转")
    private String transferMethod;

    /** 凭证日期，格式YYYY-MM-DD */
    @Excel(name = "凭证日期，格式YYYY-MM-DD")
    private String voucherDate;

    /** 凭证摘要 */
    @Excel(name = "凭证摘要")
    private String summary;

    /** 余额与方向不一致时的处理方式：0-按科目方向反向结转，1-按金额正数结转 */
    @Excel(name = "余额与方向不一致时的处理方式：0-按科目方向反向结转，1-按金额正数结转")
    private Integer directionHandling;

    /** 损益类科目的结转科目编码，关联fin_accounting_subjects.subject_code */
    @Excel(name = "损益类科目的结转科目编码，关联fin_accounting_subjects.subject_code")
    private String profitLossSubjectCode;

    /** 本年利润的结转科目编码，关联fin_accounting_subjects.subject_code */
    @Excel(name = "本年利润的结转科目编码，关联fin_accounting_subjects.subject_code")
    private String currentYearProfitSubjectCode;

    /** 以前年度损益调整科目编码，关联fin_accounting_subjects.subject_code */
    @Excel(name = "以前年度损益调整科目编码，关联fin_accounting_subjects.subject_code")
    private String priorYearAdjustmentSubjectCode;

    /** 以前年度损益调整科目的结转科目编码，关联fin_accounting_subjects.subject_code */
    @Excel(name = "以前年度损益调整科目的结转科目编码，关联fin_accounting_subjects.subject_code")
    private String priorYearAdjustTransferSubjectCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    /** 修改人 */
    @Excel(name = "修改人")
    private String modifyBy;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setTemplateId(String templateId) 
    {
        this.templateId = templateId;
    }

    public String getTemplateId() 
    {
        return templateId;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setTransferCycle(String transferCycle) 
    {
        this.transferCycle = transferCycle;
    }

    public String getTransferCycle() 
    {
        return transferCycle;
    }

    public void setTransferMethod(String transferMethod) 
    {
        this.transferMethod = transferMethod;
    }

    public String getTransferMethod() 
    {
        return transferMethod;
    }

    public void setVoucherDate(String voucherDate) 
    {
        this.voucherDate = voucherDate;
    }

    public String getVoucherDate() 
    {
        return voucherDate;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setDirectionHandling(Integer directionHandling) 
    {
        this.directionHandling = directionHandling;
    }

    public Integer getDirectionHandling() 
    {
        return directionHandling;
    }

    public void setProfitLossSubjectCode(String profitLossSubjectCode) 
    {
        this.profitLossSubjectCode = profitLossSubjectCode;
    }

    public String getProfitLossSubjectCode() 
    {
        return profitLossSubjectCode;
    }

    public void setCurrentYearProfitSubjectCode(String currentYearProfitSubjectCode) 
    {
        this.currentYearProfitSubjectCode = currentYearProfitSubjectCode;
    }

    public String getCurrentYearProfitSubjectCode() 
    {
        return currentYearProfitSubjectCode;
    }

    public void setPriorYearAdjustmentSubjectCode(String priorYearAdjustmentSubjectCode) 
    {
        this.priorYearAdjustmentSubjectCode = priorYearAdjustmentSubjectCode;
    }

    public String getPriorYearAdjustmentSubjectCode() 
    {
        return priorYearAdjustmentSubjectCode;
    }

    public void setPriorYearAdjustTransferSubjectCode(String priorYearAdjustTransferSubjectCode) 
    {
        this.priorYearAdjustTransferSubjectCode = priorYearAdjustTransferSubjectCode;
    }

    public String getPriorYearAdjustTransferSubjectCode() 
    {
        return priorYearAdjustTransferSubjectCode;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    public void setModifyBy(String modifyBy) 
    {
        this.modifyBy = modifyBy;
    }

    public String getModifyBy() 
    {
        return modifyBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateId", getTemplateId())
            .append("groupid", getGroupid())
            .append("transferCycle", getTransferCycle())
            .append("transferMethod", getTransferMethod())
            .append("voucherDate", getVoucherDate())
            .append("summary", getSummary())
            .append("directionHandling", getDirectionHandling())
            .append("profitLossSubjectCode", getProfitLossSubjectCode())
            .append("currentYearProfitSubjectCode", getCurrentYearProfitSubjectCode())
            .append("priorYearAdjustmentSubjectCode", getPriorYearAdjustmentSubjectCode())
            .append("priorYearAdjustTransferSubjectCode", getPriorYearAdjustTransferSubjectCode())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .append("createBy", getCreateBy())
            .append("modifyBy", getModifyBy())
            .toString();
    }
}
