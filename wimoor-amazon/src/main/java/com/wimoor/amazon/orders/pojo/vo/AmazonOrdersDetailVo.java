package com.wimoor.amazon.orders.pojo.vo;

import java.math.BigDecimal;
import java.util.List;

import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AmazonOrdersDetailVo对象", description="订单详情对象")
public class AmazonOrdersDetailVo {

	@ApiModelProperty(value = "订单Item状态")
	String itemstatus;
	
	@ApiModelProperty(value = "amazonAuthId")
	String amazonAuthId;
	
	@ApiModelProperty(value = "marketplaceId")
	String marketplaceId;
	
	@ApiModelProperty(value = "区域")
	String region;
	
	@ApiModelProperty(value = "币种")
	String currency;
	
	@ApiModelProperty(value = "商品图片")
	String image;
	
	@ApiModelProperty(value = "购买数量")
	Integer quantity;
	
	@ApiModelProperty(value = "SKU")
	String sku;
	
	@ApiModelProperty(value = "ASIN")
	String asin;
	
	@ApiModelProperty(value = "店铺名称")
	String groupname;
	
	@ApiModelProperty(value = "产品本地SKU颜色")
	String color;
	
	@ApiModelProperty(value = "商品名称")
	String name;
	
	@ApiModelProperty(value = "Vat税率")
	BigDecimal vatRate;
	
	@ApiModelProperty(value = "单价")
	BigDecimal itemprice;
	
	@ApiModelProperty(value = "折扣")
	BigDecimal itemdiscount;
	
	@ApiModelProperty(value = "单价税费")
	BigDecimal itemtax;
	
	@ApiModelProperty(value = "运费")
	BigDecimal shipprice;
	
	@ApiModelProperty(value = "运费折扣")
	BigDecimal shipdiscount;
	
	@ApiModelProperty(value = "总金额")
	BigDecimal totalprice;
	
	@ApiModelProperty(value = "订单金额")
	BigDecimal orderprice;
	
	@ApiModelProperty(value = "订单状态")
	String orderstatus;
	
	
	@ApiModelProperty(value = "用户名")
	String buyername;
	
	@ApiModelProperty(value = "用户邮箱")
	String buyeremail;
	
	@ApiModelProperty(value = "发货地址名称")
	String addressName;
	
	@ApiModelProperty(value = "发货地址Line")
	String addressLine;
	
	@ApiModelProperty(value = "发货地址州")
	String addressState;
	
	@ApiModelProperty(value = "发货地址城市")
	String addressCity;
	
	@ApiModelProperty(value = "发货地址国家")
	String addressCountry;
	
	@ApiModelProperty(value = "发货地址邮编")
	String addressPostal;
	
	@ApiModelProperty(value = "是否含有地址信息")
	String hasAddress;
	
	@ApiModelProperty(value = "包含的Main信息")
	AmzOrderMain orderMain;
	
	@ApiModelProperty(value = "包含的财务信息")
	List<OrdersFinancial> financialList;
	
	@ApiModelProperty(value = "账户余额变动")
	BigDecimal financialfee;
	
	

}
