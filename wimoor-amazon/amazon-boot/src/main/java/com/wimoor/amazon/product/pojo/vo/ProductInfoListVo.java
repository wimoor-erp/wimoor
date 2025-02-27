package com.wimoor.amazon.product.pojo.vo;


import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ProdoctInfoListVo对象", description="商品详情列表")
public class ProductInfoListVo {

	@ApiModelProperty(value = "materialID")
	String id;
	
	@ApiModelProperty(value = "组装库存")
	Integer willfulfillable;
	
	@ApiModelProperty(value = "SKU")
	String sku;
	
	@ApiModelProperty(value = "Asin")
	String asin;
	
	@ApiModelProperty(value = "颜色")
	String color;
	
	@ApiModelProperty(value = "名称")
	String name;
	
	@ApiModelProperty(value = "公司ID")
	String shopid;
	
	@ApiModelProperty(value = "对应本地SKU")
	String msku;
	
	@ApiModelProperty(value = "upc")
	String upc;
	
	@ApiModelProperty(value = "图片URL")
	String image;
	
	@ApiModelProperty(value = "品牌")
	String brand;
	
	@ApiModelProperty(value = "本地采购价格")
	String price;
	
	@ApiModelProperty(value = "销售价格")
	String itemprice;
	
	
	@ApiModelProperty(value = "单箱数量")
	String boxnum;
	
	@ApiModelProperty(value = "可用库存")
	String fulfillable;
	
	@ApiModelProperty(value = "MOQ")
	String MOQ;
	
	@ApiModelProperty(value = "供应商")
	String supplier;
	
	@ApiModelProperty(value = "是否组装产品")
	String issfg;

	@ApiModelProperty(value = "备注")
	String remark;
	
	@ApiModelProperty(value = "操作人")
	String operator;
	
	@ApiModelProperty(value = "操作时间")
	Date opttime;
	
	@ApiModelProperty(value = "采购链接")
	String purchaseUrl;
	
	@ApiModelProperty(value = "备货周期")
	String delivery_cycle;
	
	@ApiModelProperty(value = "长")
	BigDecimal length;
	
	@ApiModelProperty(value = "宽")
	BigDecimal width;
	
	@ApiModelProperty(value = "高")
	BigDecimal height;
	
	@ApiModelProperty(value = "重")
	BigDecimal weight;
	
	@ApiModelProperty(value = "货件校验信息")
	String guidance;
	
	@ApiModelProperty(value = "发货数量")
	Integer quantity;
	
	String pid;
	
	String fnsku;
	
	String pname;
	
	String marketplaceid;
	
	
	
	
	
}
