package com.wimoor.finance.setting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 会计科目对象 fin_accounting_subjects
 * 
 * @author wimoor
 * @date 2025-11-05
 */
public class FinAccountingSubjects extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long subjectId;

    /** 科目编码 */
    @Excel(name = "编码")
    private String subjectCode;

    /** 租户ID */
    private String groupid;

    /** 科目名称 */
    @Excel(name = "名称")
    private String subjectName;

    /** 科目级别 */
    private Integer subjectLevel;

    /** 父级科目编码 */
    private String parentCode;

    /** 科目类型 */
    private Integer subjectType;
    private Integer subjectTypeId;

    @Excel(name = "类别")
    private String subjectTypeName;

    /** 余额方向 */
    private Integer direction;

    /** 余额方向 */
    @Excel(name = "余额方向")
    private String directionName;

    /** 末级科目 */
    private Integer isLeaf;

    /** 状态 */
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedTime;

    /** 是否现金 */
    @JsonProperty("isCash")
    private Boolean isCash;


    /** 辅助科目类型 */
    @Excel(name = "辅助核算类别")
    private Long auxiliaryType;

    /** 辅助核算类型编码列表（前端传入） */
    @JsonProperty("auxiliaryTypes")
    private List<String> auxiliaryTypeList;
    
    /** 是否开启辅助核算 */
    private Boolean isAuxiliary;

     /** 是否外币 */
    @JsonProperty("isForeignCurrency")
    private Boolean isForeignCurrency;

    /** 是否开启存货明细核算 */
    @JsonProperty("isInventoryDetailEnabled")
    private Boolean isInventoryDetailEnabled;

    /** 外币核算币种 */
    @Excel(name = "外币核算")
    private String foreignCurrencys;

     /** 是否调汇 */
    private Boolean isExchange;

    /** 是否平行科目 */
    private Boolean isParallel;

    @Excel(name = "是否调汇")
    private String exchangeName;

    @Excel(name = "是否现金科目")
    private String isCashName;

    @Excel(name = "状态")
    private String statusName;

    @Excel(name = "是否平行科目")
    private String isParallelName;

    @JsonProperty("isQuantity")
    private Boolean isQuantity;

    private String subjectTypeCode;


    private String unitOfMeasure;

    public Boolean getForeignCurrency() {
        return isForeignCurrency;
    }

    public void setForeignCurrency(Boolean foreignCurrency) {
        isForeignCurrency = foreignCurrency;
    }

    public Boolean getQuantity() {
        return isQuantity;
    }

    public void setQuantity(Boolean quantity) {
        isQuantity = quantity;
    }

    public Boolean getCash() {
        return isCash;
    }

    public void setCash(Boolean cash) {
        isCash = cash;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public void setAuxiliaryType(Long auxiliaryType)
    {
        this.auxiliaryType = auxiliaryType;
    }

    public Long getAuxiliaryType()
    {
        return auxiliaryType;
    }

    public List<String> getAuxiliaryTypeList() {
        return auxiliaryTypeList;
    }

    public void setAuxiliaryTypeList(List<String> auxiliaryTypeList) {
        this.auxiliaryTypeList = auxiliaryTypeList;
    }

    public Boolean getIsAuxiliary() {
        return isAuxiliary;
    }

    public void setIsAuxiliary(Boolean isAuxiliary) {
        this.isAuxiliary = isAuxiliary;
    }

    public Boolean getIsInventoryDetailEnabled() {
        return isInventoryDetailEnabled;
    }

    public void setIsInventoryDetailEnabled(Boolean isInventoryDetailEnabled) {
        this.isInventoryDetailEnabled = isInventoryDetailEnabled;
    }

    public void setSubjectId(Long subjectId)
    {
        this.subjectId = subjectId;
    }

    public Long getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectCode(String subjectCode)
    {
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode() 
    {
        return subjectCode;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setSubjectName(String subjectName) 
    {
        this.subjectName = subjectName;
    }

    public String getSubjectName() 
    {
        return subjectName;
    }

    public void setSubjectTypeCode(String subjectTypeCode)
    {
        this.subjectTypeCode = subjectTypeCode;
    }

    public String getSubjectTypeCode()
    {
        return subjectTypeCode;
    }

    public void setSubjectLevel(Integer subjectLevel)
    {
        this.subjectLevel = subjectLevel;
    }

    public Integer getSubjectLevel()
    {
        return subjectLevel;
    }

    public void setParentCode(String parentCode) 
    {
        this.parentCode = parentCode;
    }

    public String getParentCode() 
    {
        return parentCode;
    }

    public void setSubjectType(Integer subjectType)
    {
        this.subjectType = subjectType;
    }

    public Integer getSubjectType()
    {
        return subjectType;
    }

    public void setSubjectTypeId(Integer subjectTypeId)
    {
        this.subjectTypeId = subjectTypeId;
    }

    public Integer getSubjectTypeId()
    {
        return subjectTypeId;
    }

    public void setForeignCurrencys(String foreignCurrencys) {
        this.foreignCurrencys = foreignCurrencys;
    }

    public String getForeignCurrencys() {
        return foreignCurrencys;
    }

    public void setDirection(Integer direction)
    {
        this.direction = direction;
    }

    public Integer getDirection()
    {
        return direction;
    }

    public void setIsLeaf(Integer isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public Integer getIsLeaf()
    {
        return isLeaf;
    }

    public void setIsExchange(Boolean isExchange) {
        this.isExchange = isExchange;
    }

    public Boolean getIsExchange() {
        return isExchange;
    }

    public void setIsParallel(Boolean isParallel) {
        this.isParallel = isParallel;
    }
    public Boolean getIsParallel() {
        return isParallel;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
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

    public void setSubjectTypeName(String subjectTypeName){this.subjectTypeName = subjectTypeName;}

    public String getSubjectTypeName(){return subjectTypeName;}

    public void setIsCashName(String isCashName){this.isCashName = isCashName;}

    public String getIsCashName(){return isCashName;}

    public void setDirectionName (String directionName){this.directionName = directionName;}

    public String getDirectionName(){return directionName;}

    public void setExchangeName(String exchangeName){this.exchangeName = exchangeName;}

    public String getExchangeName(){return exchangeName;}


    public String getStatusName(){return statusName;}

    public void setStatusName(String statusName){this.statusName = statusName;}

    public String getIsParallelName(){return isParallelName;}

    public void setIsParallelName(String isParallelName){this.isParallelName = isParallelName;}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("subjectId", getSubjectId())
            .append("subjectCode", getSubjectCode())
            .append("groupid", getGroupid())
            .append("subjectName", getSubjectName())
            .append("subjectLevel", getSubjectLevel())
            .append("parentCode", getParentCode())
            .append("subjectType", getSubjectType())
            .append("direction", getDirection())
            .append("isLeaf", getIsLeaf())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }

    public boolean isDebitBalance() {
        return direction == 1;
    }
}
