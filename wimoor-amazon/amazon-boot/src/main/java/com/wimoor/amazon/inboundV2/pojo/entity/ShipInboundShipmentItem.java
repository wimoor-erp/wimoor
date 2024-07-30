package com.wimoor.amazon.inboundV2.pojo.entity;

import java.math.BigDecimal;
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
@TableName("t_erp_ship_v2_inboundshipment_item")
public class ShipInboundShipmentItem extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3072300909415719829L;

	@ApiModelProperty(value = "订单ID【订单填写】")
	@TableField(value="shipmentid")
    private String shipmentid;

	@ApiModelProperty(value = "asin")
	@TableField(value="asin")
    private String asin;
	
	@TableField(value="expiration")
    private Date expiration;
	
	@ApiModelProperty(value = "ERP本地SKU【订单填写】")
	@TableField(value="fnsku")
    private String fnsku;
	
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
 
	@ApiModelProperty(value = "MSKU")
	@TableField(value="msku")
    private String msku;
	
	@ApiModelProperty(value = "订单数量【订单填写】")
	@TableField(value="quantity")
    private Integer quantity;
	
	@ApiModelProperty(value = "接受数量")
	@TableField(value="received")
    private Integer received;
	
	@ApiModelProperty(value = "prepinstructions")
	@TableField(value="prepinstructions")
    private String prepinstructions;
	
	@ApiModelProperty(value = "totaltransfee")
	@TableField(value="totaltransfee")
	private BigDecimal totaltransfee;
	
	@ApiModelProperty(value = "unittransfee")
	@TableField(value="unittransfee")
	private BigDecimal unittransfee;
	
	@ApiModelProperty(value = "totalcost")
	@TableField(value="totalcost")
	private BigDecimal totalcost;
	
	@ApiModelProperty(value = "unitcost")
	@TableField(value="unitcost")
	private BigDecimal unitcost;
 
	@ApiModelProperty(value = "operator")
	@TableField(value="operator")
    private String operator;
	
	@ApiModelProperty(value = "opttime")
	@TableField(value="opttime")
    private Date opttime;
	
	@ApiModelProperty(value = "receiveddate")
	@TableField(value="receiveddate")
    private Date receiveddate;
	
	
}