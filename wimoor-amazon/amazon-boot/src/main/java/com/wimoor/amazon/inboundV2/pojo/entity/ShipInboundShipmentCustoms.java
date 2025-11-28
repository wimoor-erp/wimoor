package com.wimoor.amazon.inboundV2.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ShipInboundCustoms对象", description="货件海关信息")
@TableName("t_erp_ship_v2_inboundshipment_customs")
public class ShipInboundShipmentCustoms  {
 
	@ApiModelProperty(value = "计划ID")
	@TableId(value= "itemid")
    private String itemid;
	
	@ApiModelProperty(value = "price")
	@TableField(value= "price")
    private BigDecimal price;
	
	@ApiModelProperty(value = "code")
	@TableField(value= "code")
    private String code;
	
	@ApiModelProperty(value = "rate")
	@TableField(value= "rate")
    private BigDecimal rate;
	
	@ApiModelProperty(value = "material")
    @TableField(value= "material")
    private String material;

    @ApiModelProperty(value = "产品材质")
    @TableField(value= "materialcn")
    private String materialcn;

	@ApiModelProperty(value = "application")
    @TableField(value= "application")
    private  String application;

	@ApiModelProperty(value = "url")
    @TableField(value= "url")
    private String url;

	@ApiModelProperty(value = "ename")
    @TableField(value= "ename")
    private String ename;

	@ApiModelProperty(value = "cname")
    @TableField(value= "cname")
    private String cname;

	@ApiModelProperty(value = "creator")
    @TableField(value= "creator")
    private String creator;

	@ApiModelProperty(value = "createtime")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE,value = "createtime")
    private Date createtime;
     
	@TableField(value = "operator")
    private String operator;
 
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE,value = "opttime")
    private Date opttime;

    @TableField(exist = false)
    private String sku;
	
}