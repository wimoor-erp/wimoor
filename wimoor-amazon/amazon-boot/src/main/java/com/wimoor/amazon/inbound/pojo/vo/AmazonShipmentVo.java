package com.wimoor.amazon.inbound.pojo.vo;


import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="asyncShipment对象", description="发货货件")
public class AmazonShipmentVo {
   @ApiModelProperty(value = "店铺ID")
   String groupid;
   @ApiModelProperty(value = "店铺名称")
   String groupname;
   @ApiModelProperty(value = "国家编码")
   String country;
   @ApiModelProperty(value = "站点编码")
   String marketplaceid;
   @ApiModelProperty(value = "国家名称，站点名称")
   String marketname;
   @ApiModelProperty(value = "货件")
   InboundShipmentInfo info;
}
