package com.wimoor.erp.purchase.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseTimeDTO对象", description="采购时效")
public class PurchaseTimeDTO extends BasePageQuery {
    private String sku;

    private String shopid;

    private String warehouseid;

    @ApiModelProperty(value = "开始日期", example = "2022-01-01")
    String startDate ;

    @ApiModelProperty(value = "结束日期", example = "2022-01-01")
    String endDate ;
}
