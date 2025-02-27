package com.wimoor.amazon.inboundV2.pojo.vo;



import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="asyncShipment对象", description="发货计划")
public class SummaryPlanVo {

   @ApiModelProperty(value = "SKU数量")
   String skucount;
   @ApiModelProperty(value = "发货数量")
   String toquantity;
   @ApiModelProperty(value = "ID")
   String formid;
   @ApiModelProperty(value = "货件名称")
   String name;
   @ApiModelProperty(value = "预估重量")
   String readweight;
   @ApiModelProperty(value = "预估重量单位")
   String readunit;
   @ApiModelProperty(value = "状态")
   String status;
   @ApiModelProperty(value = "重量")
   String weight;
   @ApiModelProperty(value = "箱子体积")
   String boxvolume;
   @ApiModelProperty(value = "重量单位")
   String weightunit;
   @ApiModelProperty(value = "货值")
   String itemprice;
   @ApiModelProperty(value = "备注")
   String remark;
   @ApiModelProperty(value = "发货方式SP小包裹，或是LTL托盘")
   String transtyle;
   @ApiModelProperty(value = "公司名称")
   String company;
   @ApiModelProperty(value = "渠道名称")
   String channel;
   @ApiModelProperty(value = "公司ID")
   String companyid;
   @ApiModelProperty(value = "渠道ID")
   String channelid;
   @ApiModelProperty(value = "产品列表")
   List<ShipInboundItemVo> itemlist;
}
