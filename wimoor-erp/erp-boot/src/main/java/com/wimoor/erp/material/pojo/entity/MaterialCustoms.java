package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="MaterialCustoms对象", description="产品报关表")
@TableName("t_erp_material_customs")
public class MaterialCustoms {
	
	@ApiModelProperty(value = "产品ID")
	@TableId(value= "materialid")
    private String materialid;
	
	@ApiModelProperty(value = "产品报关币种")
	@TableField(value= "currency")
    private String currency;

	@ApiModelProperty(value = "产品报关英文名")
	@TableField(value= "name_en")
    private String nameEn;

	@ApiModelProperty(value = "产品报关中文名")
	@TableField(value= "name_cn")
    private String nameCn;

	@ApiModelProperty(value = "产品材质")
	@TableField(value= "material")
    private String material;

	@ApiModelProperty(value = "产品模型")
	@TableField(value= "model")
    private String model;

	@ApiModelProperty(value = "海关编码")
	@TableField(value= "customs_code")
    private String customsCode;

	@ApiModelProperty(value = "海关编码")
	@TableField(value= "material_use")
    private String materialUse;

	@ApiModelProperty(value = "是否带电")
	@TableField(value= "iselectricity")
    private Boolean iselectricity;

	@ApiModelProperty(value = "品牌")
	@TableField(value= "brand")
    private String brand;
	
	@ApiModelProperty(value = "是否危险")
	@TableField(value= "isdanger")
    private Boolean isdanger;
	
	@ApiModelProperty(value = "报关价格")
	@TableField(value= "unitprice")
    private BigDecimal unitprice;
	
	@ApiModelProperty(value = "附加费用")
	@TableField(value= "addfee")
    private BigDecimal addfee;
	
}