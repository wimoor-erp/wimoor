package com.wimoor.amazon.inbound.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipTimeDTO对象", description="货件时效")
public class ShipTimeDTO extends BasePageQuery {

    private String sku;

    private String msku;

    private String shopid;

    private String groupid;

    private String marketplaceid;

    @ApiModelProperty(value = "开始日期", example = "2022-01-01")
    String startDate ;

    @ApiModelProperty(value = "结束日期", example = "2022-01-01")
    String endDate ;
}
