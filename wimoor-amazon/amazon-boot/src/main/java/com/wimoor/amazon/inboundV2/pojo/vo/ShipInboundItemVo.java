package com.wimoor.amazon.inboundV2.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wimoor.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundItemVo对象", description="货件ItemVo")
public class ShipInboundItemVo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3072300909415719829L;

	@ApiModelProperty(value = "本地产品sku")
    private String sku;
 
	@ApiModelProperty(value = "单箱数量")
    private Integer boxnum;
	 
	@ApiModelProperty(value = "箱子长度cm")
    private BigDecimal boxlength;
	
	@ApiModelProperty(value = "箱子宽度cm")
    private BigDecimal boxwidth;
	
	@ApiModelProperty(value = "箱子高度cm")
    private BigDecimal boxheight;
	
	@ApiModelProperty(value = "箱子重量kg")
    private BigDecimal boxweight;
	
	@ApiModelProperty(value = "箱子重量kg")
    private BigDecimal pkgweight;
	
	@ApiModelProperty(value = "长度cm")
    private BigDecimal pkglength;
	
	@ApiModelProperty(value = "宽度cm")
    private BigDecimal pkgwidth;
	
	@ApiModelProperty(value = "高度cm")
    private BigDecimal pkgheight;
	
	@ApiModelProperty(value = "体积")
    private BigDecimal boxvolume;
	
	@ApiModelProperty(value = "产品体积")
    private BigDecimal volume;
	
	@ApiModelProperty(value = "sku产品体积")
    private BigDecimal skuvolume;
	
	@ApiModelProperty(value = "产品体积重")
    private BigDecimal dimweight;
	
	@ApiModelProperty(value = "产品重量")
    private BigDecimal weight;
	
	@ApiModelProperty(value = "货值")
    private BigDecimal price;
	
	@ApiModelProperty(value = "是否组合产品")
    private Integer issfg;
	 
	@ApiModelProperty(value = "商品ASIN")
    private String asin;
	
	@ApiModelProperty(value = "产品库存")
    private Long invquantity;

	@ApiModelProperty(value = "产品待出库库存")
    private Long outbound; 
	
	@ApiModelProperty(value = "本地产品ID")
    private String materialid;

	@ApiModelProperty(value = "本地产品ID")
	private String typename;

	@ApiModelProperty(value = "平台SKU")
	@TableField(value="SellerSKU")
    private String sellersku;
	
	@ApiModelProperty(value = "订单数量")
	@TableField(value="quantity")
    private Integer quantity;

	@ApiModelProperty(value = "发货量【系统内置】")
	@TableField(value="confirm_quantity")
    private Integer confirmQuantity;
	
	@ApiModelProperty(value = "产品名称")
    private String name;
	
	@ApiModelProperty(value = "平台商品名称")
    private String pname;
	
	@ApiModelProperty(value = "图片路径")
    private String image;
	
	@ApiModelProperty(value = "日均销量")
    private Integer avgsales;
	
	@ApiModelProperty(value = "平台SKU【订单填写】")
	@TableField(value="msku")
	private String msku;
 
	@ApiModelProperty(value = "平台配送SKU【订单填写】")
	@TableField(value="fnsku")
	private String fnsku;
 
	@TableField(value="unitcost")
    private BigDecimal unitcost;
	
	@TableField(value="totalcost")
    private BigDecimal totalcost;
	
	
	String  labelOwner;
	String  prepOwner;
	Date  expiration;
	String manufacturingLotCode;

}