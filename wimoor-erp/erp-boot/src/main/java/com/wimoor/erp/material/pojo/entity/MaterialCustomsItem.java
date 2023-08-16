package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="MaterialCustomsItem对象", description="产品报关表item")
@TableName("t_erp_material_customs_item")
public class MaterialCustomsItem {

	@ApiModelProperty(value = "产品ID")
	@TableId(value= "materialid")
    private String materialid;

	@ApiModelProperty(value = "产品报关国家code")
	@TableField(value= "country")
    private String country;
	
	@ApiModelProperty(value = "产品报关币种")
	@TableField(value= "currency")
    private String currency;

	@ApiModelProperty(value = "海关编码")
	@TableField(value= "code")
    private String code;
 
	@ApiModelProperty(value = "报关头程费用")
	@TableField(value= "fee")
    private BigDecimal fee;
	
	@ApiModelProperty(value = "税率")
	@TableField(value= "taxrate")
    private BigDecimal taxrate;
}
