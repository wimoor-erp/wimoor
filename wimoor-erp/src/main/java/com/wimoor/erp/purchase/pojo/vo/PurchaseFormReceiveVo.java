
package com.wimoor.erp.purchase.pojo.vo;



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
@ApiModel(value="PurchaseFormReceiveVo对象", description="采购入库明细报表")
public class PurchaseFormReceiveVo {
	@ApiModelProperty(value = "操作时间")
	Date opttime;
	
	@ApiModelProperty(value = "操作数量")
	Integer amount;
	
	@ApiModelProperty(value = "采购单编码")
	String number;
	
	@ApiModelProperty(value = "创建时间")
	Date createdate;
	
	@ApiModelProperty(value = "产品SKU")
	String sku;
	
	@ApiModelProperty(value = "操作类型")
	String ftype;
	
	@ApiModelProperty(value = "操作备注")
	String remark;
	
	@ApiModelProperty(value = "需要入库数量")
	Integer needin;
	
	@ApiModelProperty(value = "操作人")
	String name;
	
	@ApiModelProperty(value = "供应商")
	String cname;
	
	@ApiModelProperty(value = "操作仓库")
	String wname;
	
	
	@ApiModelProperty(value = "产品图片")
	String image;
	
	@ApiModelProperty(value = "产品名称")
	String mname;
	
	@ApiModelProperty(value = "采购数量")
	Integer purchases;
	
	@ApiModelProperty(value = "采购金额")
	BigDecimal purchaseprice;
	
	@ApiModelProperty(value = "入库数量汇总")
	Integer totalamount;
	
	@ApiModelProperty(value = "审核日期")
	Date audittime;
	
	@ApiModelProperty(value = "预计到货日期")
	Date deliverydate;
	
	
	
}

