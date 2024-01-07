package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MaterialConsumable对象", description="产品耗材关系表")
@TableName("t_erp_material_consumable")
public class MaterialConsumable extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4326333404010707981L;
	@ApiModelProperty(value = "产品ID")
	@TableField(value="materialid")
    private String materialid;

	@ApiModelProperty(value = "耗材产品ID")
	@TableField(value="submaterialid")
    private String submaterialid;

	@ApiModelProperty(value = "单个产品对应的耗材数量")
	@TableField(value="amount")
    private BigDecimal amount;

	@ApiModelProperty(value = "修改人ID【系统填写】")
	@TableField(value="operator")
    private String operator;

	@ApiModelProperty(value = "修改时间【系统填写】")
	@TableField(value="opttime")
    private Date opttime;

	public String getMaterialid() {
		return materialid;
	}

	public void setMaterialid(String materialid) {
		this.materialid = materialid;
	}

	public String getSubmaterialid() {
		return submaterialid;
	}

	public void setSubmaterialid(String submaterialid) {
		this.submaterialid = submaterialid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

   
}