package com.wimoor.amazon.performance.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CouponPerformanceReportListDTO", description="获取Coupon列表")
public class PerformanceReportListDTO extends BasePageQuery {
    @ApiModelProperty(value = "站点ID")
    String marketplaceid;

    @ApiModelProperty(value = "店铺ID")
    String groupid;

    @ApiModelProperty(value = "查询内容")
    String search;

    @ApiModelProperty(value = "内部使用，无需填写")
    String shopid;
}
