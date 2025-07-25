package com.wimoor.amazon.orders.pojo.dto;

import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmazonOrdersDTO对象", description="获取订单列表")
public class OrdersFulfillmentDTO extends BasePageQuery {
    @ApiModelProperty(value = "开始日期")
    String startDate;

    @ApiModelProperty(value = "结束日期")
    String endDate;

    @ApiModelProperty(value = "店铺ID[全选时为all]")
    String groupid;

    @ApiModelProperty(value = "AuthID[系统填充]")
    String amazonAuthId;

    @ApiModelProperty(value = "站点ID[系统填充]")
    String marketplaceid;

    @ApiModelProperty(value = "orderid[系统填充]")
    String orderid;

    @ApiModelProperty(value = "订单状态")
    String status;

    @ApiModelProperty(value = "公司ID[系统填充]")
    String shopid;
}
