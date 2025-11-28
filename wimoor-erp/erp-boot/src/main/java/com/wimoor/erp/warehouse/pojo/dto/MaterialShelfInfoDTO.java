package com.wimoor.erp.warehouse.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value=" MaterialShelfInfoDTO对象", description="用于扫描二维码时调用接口获取产品信息查询")
public class MaterialShelfInfoDTO {

    @ApiModelProperty(value = "信息类型 rec, rec-sku, rec-po, rec-dis ")
    String infotype;

    @ApiModelProperty(value = "产品ID ")
    String materialid;

    @ApiModelProperty(value = "组装单ID ")
    String assemblyFormid;

    @ApiModelProperty(value = "入库单ID ")
    String purchaseFormRecId;

    @ApiModelProperty(value = "调库单ID ")
    String dispatchFormid;

    @ApiModelProperty(value = "仓库ID ")
    String warehouseid ;

    @ApiModelProperty(value = "操作数量")
    String quantity;
}
