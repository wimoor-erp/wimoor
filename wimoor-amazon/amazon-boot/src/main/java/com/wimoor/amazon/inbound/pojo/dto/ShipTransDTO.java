package com.wimoor.amazon.inbound.pojo.dto;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ShipTransDTO对象", description="货件物流")
public class ShipTransDTO{
	
	@ApiModelProperty(value = "货件ID")
	String shipmentid;
	
	@ApiModelProperty(value = "公司ID")
	String company;
	
	@ApiModelProperty(value = "渠道ID")
	String channel;
	
	@ApiModelProperty(value = "重量单位[kg,cbm]")
	String unit;
	
	@ApiModelProperty(value = "结算类型[0.重量 1.体积]")
	String wtype;
	
	@ApiModelProperty(value = "运输方式ID")
	String transtype;
	
	@ApiModelProperty(value = "追踪编号")
	String ordernum;
	
	@ApiModelProperty(value = "备注")
	String remark;
	
	@ApiModelProperty(value = "操作类型 save:保存 confirm:提交出库")
	String operate;
	
	@ApiModelProperty(value = "当为LTL发货时 填写的Pro_number")
	String proNumber;
	
	@ApiModelProperty(value = "配送商ID")
	String carrier;
	
	@ApiModelProperty(value = "结算重量")
	BigDecimal rweight;
	
	@ApiModelProperty(value = "单价")
	BigDecimal singleprice;
	
	@ApiModelProperty(value = "关税等其它费用")
	BigDecimal otherfee;

	@ApiModelProperty(value = "预计到货日期")
	Date arrive;
	
	@ApiModelProperty(value = "货件发货日期")
	Date shipdate;
	
	@ApiModelProperty(value = "货件出港日期")
	Date outarrivaldate;
	
	@ApiModelProperty(value = "货件到港日期")
	Date inarrivaldate;
	
	@ApiModelProperty(value = "货件追踪信息")
	List<Map<String, Object>> boxinfo;
	
	
	

	
}
