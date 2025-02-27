package com.wimoor.amazon.inboundV2.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundItem对象", description="货件Item")
@TableName("t_erp_ship_v2_inboundshipment_boxitem")
public class ShipInboundShipmentBoxItem extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3072300909415719829L;

	@ApiModelProperty(value = "订单ID【订单填写】")
	@TableField(value="boxid")
    private String boxid;
	
	@TableField(value="expiration")
    private Date expiration;

	@ApiModelProperty(value = "贴标【AMAZON，SELLER】")
	@TableField(value="label_owner")
    private String labelOwner;
 
	@ApiModelProperty(value = "manufacturingLotCode")
	@TableField(value="manufacturing_lot_code")
    private String manufacturingLotCode;
	
	@ApiModelProperty(value = "预备信息处理人【AMAZON，SELLER】")
	@TableField(value="prep_owner")
    private String prepOwner;
	
	@ApiModelProperty(value = "平台SKU【订单填写】")
	@TableField(value="sku")
    private String sku;
	
	@ApiModelProperty(value = "订单数量【订单填写】")
	@TableField(value="quantity")
    private Integer quantity;
	
	@ApiModelProperty(value = "发货量【系统内置】")
	@TableField(value="confirm_quantity")
    private Integer confirmQuantity;

	@TableField(value="operator")
    private String operator;
	
	@TableField(value="opttime")
    private Date opttime;

	
	
	
	
}