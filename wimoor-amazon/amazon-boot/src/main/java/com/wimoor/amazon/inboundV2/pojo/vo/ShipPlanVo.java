package com.wimoor.amazon.inboundV2.pojo.vo;


import java.util.Date;
import java.util.List;

import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ShipPlanListVo对象", description="发货货件计划")
public class ShipPlanVo  {
    
	 @ApiModelProperty(value = "计划名称")
	 String name;
	 
	 @ApiModelProperty(value = "创建日期【yyyy-MM-dd】")
	 Date createtime;
	 
	 @ApiModelProperty(value = "地址id")
	 String addressid;
	 
	 @ApiModelProperty(value = "仓库名称")
	 String warename;
	 
	 @ApiModelProperty(value = "国家编码")
	 String country;

	@ApiModelProperty(value = "国家名称")
	String countryname;

	 @ApiModelProperty(value = "创建人")
	 String creatorname;
	 
	 @ApiModelProperty(value = "SKU数量")
	 Integer skunum;
	 
	 @ApiModelProperty(value = "发货总数量")
	 Integer sumquantity;
	 
	 @ApiModelProperty(value = "发货国家")
	 String destination;
	 
	 @ApiModelProperty(value = "店铺名称")
	 String groupname;
	 
	 @ApiModelProperty(value = "计划ID")
	 String id;
	 
	 @ApiModelProperty(value = "计划编码")
	 String number;
	 
	 @ApiModelProperty(value = "退货地址ID")
	 String shipFromAddressID;
	 
	 @ApiModelProperty(value = "placement_option_id")
	 String placementOptionId;
	 
	 @ApiModelProperty(value = "packing_option_id")
	 String packingOptionId;
	 
	 @ApiModelProperty(value = "入库货件所需的标签准备类型")
	 String labelPrepType;
	 
	 @ApiModelProperty(value = "是否混装发货")
	 Boolean AreCasesRequired;
	 
	 @ApiModelProperty(value = "店铺ID")
	 String amazongroupid;
	 
	 @ApiModelProperty(value = "站点")
	 String marketplaceid;
	 
	 @ApiModelProperty(value = "authid")
	 String amazonauthid;
	 
	 @ApiModelProperty(value = "仓库ID")
	 String warehouseid;
	 
	 @ApiModelProperty(value = "公司ID")
	 String shopid;

	 @ApiModelProperty(value = "发货方式")
	 String transtypename;

	 Integer days;
	 @ApiModelProperty(value = "备注")
	 String remark;
	 
	 @ApiModelProperty(value = "计划状态")
	 Integer auditstatus;
	 
	 @ApiModelProperty(value = "审核时间")
	 Date auditime;

	 @ApiModelProperty(value = "发货日期")
	 Date shippingDate;
	 @ApiModelProperty(value = "计划SubID")
	 String plansubid;
	 
	 @ApiModelProperty(value = "操作时间")
	 Date opttime;
	 
	@ApiModelProperty(value = "库存状态，是否已经扣除库存")
    String invstatus;
	
	@ApiModelProperty(value = "库存操作方式，是否要操作库存")
    String invtype;
	
	@ApiModelProperty(value = "配货单号")
    String checkInv;

	String inboundPlanId;

    Integer transday;

	Integer sumdays;

     Boolean submitbox;

	 @ApiModelProperty(value = "发货地址")
	 ShipAddress address;

	 @ApiModelProperty(value = "是否已经提交箱子信息")
	 Boolean hasSubmitPackage;

	 @ApiModelProperty(value = "计划汇总")
	 SummaryPlanVo plansummary;
 
     List<ShipInboundItemVo> itemlist;
}
