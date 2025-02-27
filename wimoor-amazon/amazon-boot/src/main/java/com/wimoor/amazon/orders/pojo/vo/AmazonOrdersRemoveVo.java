package com.wimoor.amazon.orders.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AmazonOrdersRemoveVo对象", description="订单移除列表对象")
public class AmazonOrdersRemoveVo {

	@ApiModelProperty(value = "移除订单ID")
	String orderId;
	
	@ApiModelProperty(value = "移除订单时间")
	Date purchaseDate;
	
	@ApiModelProperty(value = "移除订单最后更新时间")
	Date lastUpdatedDate;
	
	@ApiModelProperty(value = "订单类型")
	String orderType;
	
	@ApiModelProperty(value = "订单状态")
	String orderStatus;
	
	@ApiModelProperty(value = "商品处置[Unsellable,Sellable]")
	String disposition;
	
	@ApiModelProperty(value = "请求移除的数量")
	Integer requestedQuantity;
	
	@ApiModelProperty(value = "取消移除的数量")
	Integer cancelledQuantity;
	
	@ApiModelProperty(value = "被弃置的数量")
	Integer disposedQuantity;
	
	@ApiModelProperty(value = "退回的数量")
	Integer shippedQuantity;
	
	@ApiModelProperty(value = "处理中的数量")
	Integer inProcessQuantity;
	
	@ApiModelProperty(value = "商品图片")
	String image;
	
	@ApiModelProperty(value = "SKU")
	String sku;
	
	@ApiModelProperty(value = "ASIN")
	String asin;
	
	@ApiModelProperty(value = "平台SKU")
	String fnsku;
	
	@ApiModelProperty(value = "店铺名称")
	String groupname;
	
	@ApiModelProperty(value = "销售区域[NA,FE,EU.....]")
	String region;
	
	@ApiModelProperty(value = "商品名称")
	String name;
	
	@ApiModelProperty(value = "amazonAuthId")
	String amazonAuthId;
	
	@ApiModelProperty(value = "移除费用")
	BigDecimal removalFee;
	
	@ApiModelProperty(value = "币种")
	String currency;
	
	
}
