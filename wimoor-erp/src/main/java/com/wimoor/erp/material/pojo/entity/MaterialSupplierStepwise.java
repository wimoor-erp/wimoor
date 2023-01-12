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
@ApiModel(value="MaterialSupplierStepwise对象", description="产品对应供应商的阶梯采购价")
@TableName("t_erp_material_supplier_stepwise")
public class MaterialSupplierStepwise extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5790008895133638125L;

	@ApiModelProperty(value = "产品ID")
	@TableField(value="materialid")
    private String materialid;

	@ApiModelProperty(value = "供应商ID")
    @TableField(value="supplierid")
    private String supplierid;

	@ApiModelProperty(value = "价格")
    @TableField(value="price")
    private BigDecimal price;

	@ApiModelProperty(value = "数量")
    @TableField(value="amount")
    private Integer amount;

	@ApiModelProperty(value = "修改人【系统填写】")
    @TableField(value="operator")
    private String operator;

	@ApiModelProperty(value = "数量")
    @TableField(value="opttime")
    private Date opttime;
	
	@ApiModelProperty(value = "币别")
    @TableField(value="currency")
    private String currency;
 
}