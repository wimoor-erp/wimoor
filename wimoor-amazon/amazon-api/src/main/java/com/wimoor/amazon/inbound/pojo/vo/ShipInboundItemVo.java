package com.wimoor.amazon.inbound.pojo.vo;

import java.math.BigDecimal;
import java.util.List;

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
	
	@ApiModelProperty(value = "货件ID")
    private String ShipmentId;
	
	String typename;
	@ApiModelProperty(value = "亚马逊仓库SKU")
	@TableField(value="FNSKU")
    private String FNSKU;

	@ApiModelProperty(value = "发货量")
    private Integer QuantityShipped;

	@ApiModelProperty(value = "发货量")
	@TableField(value="QuantityInCase")
    private Integer QuantityInCase;
	
	@ApiModelProperty(value = "接收数量")
	@TableField(value="QuantityReceived")
    private Integer QuantityReceived;

	@ApiModelProperty(value = "产品颜色")
    private String mcolor;
 
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
	
	@ApiModelProperty(value = "平台SKU")
	@TableField(value="SellerSKU")
    private String sellersku;
	
	@ApiModelProperty(value = "订单数量")
	@TableField(value="quantity")
    private Integer quantity;

	@ApiModelProperty(value = "产品名称")
    private String name;
	
	@ApiModelProperty(value = "平台商品名称")
    private String pname;
	
	@ApiModelProperty(value = "图片路径")
    private String image;
	
	
	@ApiModelProperty(value = "日均销量")
    private Integer avgsales;
	

	@ApiModelProperty(value = "是否label【系统内置】")
	@TableField(value="PrepInstruction")
    private String prepInstruction;
	
	@ApiModelProperty(value = "打label的人【系统内置】")
	@TableField(value="PrepOwner")
    private String prepOwner;
	
	 
	@ApiModelProperty(value = "平台SKU【订单填写】")
	@TableField(value="msku")
	private String msku;
 
	
	@ApiModelProperty(value = "货架库存")
	List<WarehouseShelfInventoryVo> shelfInvList;
	List<WarehouseShelfInventoryOptRecordVo> shelfInvRecordList;
	@ApiModelProperty(value = "组装列表")
	List<AssemblyVO> assemblyList;
	
	@TableField(value="unitcost")
    private BigDecimal unitcost;
	
	@TableField(value="totalcost")
    private BigDecimal totalcost;
	
	@TableField(value="unittransfee")
    private BigDecimal unittransfee;
	
	@TableField(value="totaltransfee")
    private BigDecimal totaltransfee;
	
	
	
}