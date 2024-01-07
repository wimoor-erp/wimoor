package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="StepWisePrice对象", description="阶梯采购价")
@TableName("t_erp_stepwise_quotn")
public class StepWisePrice extends ErpBaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7253545273031706231L;

	@ApiModelProperty(value = "产品ID")
	@TableField(value= "material")
    private String material;

	@ApiModelProperty(value = "数量")
	@TableField(value= "amount")
    private Integer amount;

	@ApiModelProperty(value = "价格")
	@TableField(value= "price")
    private BigDecimal price;

 
}