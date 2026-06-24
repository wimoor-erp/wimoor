package com.wimoor.manager.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户订单查询DTO
 */
@Data
@ApiModel("客户订单查询DTO")
public class CustomerOrderQueryDTO extends BasePageQuery {


    @ApiModelProperty(value = "搜索关键词（订单号、订单名称、公司名称、套餐名称）")
    private String search;

    @ApiModelProperty(value = "订单类型（package/append）")
    private String ftype;

    @ApiModelProperty(value = "付款状态（success/pending/failed）")
    private String paystatus;

    @ApiModelProperty(value = "开票状态（invoiced/pending）")
    private String ivcstatus;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "公司ID")
    private String shopid;

    @ApiModelProperty(value = "套餐ID")
    private Integer tariffpackage;

    @ApiModelProperty(value = "是否有开票请求")
    private Boolean hasInvoiceRequest;
}
