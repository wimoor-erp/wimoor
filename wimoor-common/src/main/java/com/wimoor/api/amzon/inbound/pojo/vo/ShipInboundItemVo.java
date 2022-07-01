package com.wimoor.api.amzon.inbound.pojo.vo;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wimoor.api.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.api.erp.warehouse.pojo.vo.WarehouseShelfInventoryVo;
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
	
	@ApiModelProperty(value = "亚马逊仓库SKU")
	@TableField(value="FNSKU")
    private String FNSKU;

	@ApiModelProperty(value = "发货量")
    private Integer QuantityShipped;

	@ApiModelProperty(value = "发货量")
	@TableField(value="QuantityInCase")
    private Integer QuantityInCase;
	
	@ApiModelProperty(value = "接收数量")
	@TableField(value="quantityreceived")
    private Integer quantityreceived;

	@ApiModelProperty(value = "产品颜色")
    private String mcolor;
 
	@ApiModelProperty(value = "单箱数量")
    private Integer boxnum;
	 
	@ApiModelProperty(value = "箱子长度cm")
    private Integer boxlength;
	
	@ApiModelProperty(value = "箱子宽度cm")
    private Integer boxwidth;
	
	@ApiModelProperty(value = "箱子高度cm")
    private Integer boxheight;
	
	@ApiModelProperty(value = "箱子重量kg")
    private Integer boxweight;
	
	
	@ApiModelProperty(value = "是否组合产品")
    private Integer issfg;
	 
	@ApiModelProperty(value = "商品ASIN")
    private String asin;
	
	@ApiModelProperty(value = "产品库存")
    private Long invquantity;

	@ApiModelProperty(value = "产品待出库库存")
    private Long invquantity_outbound; 
	
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
	
	
	@ApiModelProperty(value = "货架库存")
	List<WarehouseShelfInventoryVo> shelfInvList;
	
	@ApiModelProperty(value = "货架库存")
	List<AssemblyVO> assemblyList;
}