package com.wimoor.erp.material.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value="MaterialCustoms对象", description="产品报关表")
@TableName("t_erp_material_custom")
public class MaterialCustom {
	
	@ApiModelProperty(value = "产品ID")
	@TableId(value= "materialid")
    private String materialid;

	@ApiModelProperty(value = "国家")
	@TableField(exist = false)
	private String country;

	@ApiModelProperty(value = "报关价格")
	@TableField(value= "price")
    private BigDecimal price;
	
	@ApiModelProperty(value = "税率")
	@TableField(value= "rate")
    private BigDecimal rate;
	
	@ApiModelProperty(value = "海关编码")
	@TableField(value= "code")
    private String code;
	
	@ApiModelProperty(value = "产品材质")
	@TableField(value= "material")
    private String material;

	@ApiModelProperty(value = "产品材质")
	@TableField(value= "materialcn")
	private String materialcn;

	@ApiModelProperty(value = "用途")
	@TableField(value= "application")
    private String application;
	
	@ApiModelProperty(value = "链接")
	@TableField(value= "url")
    private String url;

	@ApiModelProperty(value = "单位")
	@TableField(value= "unit")
	private String unit;

	@ApiModelProperty(value = "子单位")
	@TableField(value= "unit2")
	private String unit2;

	@ApiModelProperty(value = "子单位比例")
	@TableField(value= "unitrate")
	private BigDecimal unitrate;

	@ApiModelProperty(value = "产品报关英文名")
	@TableField(value= "ename")
	private String ename;

	@ApiModelProperty(value = "海关申报要素")
	@TableField(value= "elements")
    private String elements;

	@ApiModelProperty(value = "产品报关中文名")
	@TableField(value= "cname")
    private String cname;
	
	@TableField(value= "operator")
    private String operator;
	
	@TableField(value= "creator")
    private String creator;

	@TableField(value= "opttime")
	private Date opttime;
	
	@TableField(value= "createtime")
	private Date createtime;
 
	

	
}