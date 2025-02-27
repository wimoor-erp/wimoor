package com.wimoor.erp.warehouse.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value=" MaterialShelfInfoVo对象", description="用于扫描二维码时调用接口获取产品信息展示")
public class MaterialShelfInfoVo {

    @ApiModelProperty(value = "产品SKU ")
    String sku;

    @ApiModelProperty(value = "产品图片")
    String image;

    @ApiModelProperty(value = "产品名称")
    String name;

    @ApiModelProperty(value = "上架数量")
    String quantity;

    @ApiModelProperty(value = "仓库名称")
    String warehousename;

    @ApiModelProperty(value = "货架树结构")
    List<WarehouseShelfTreeVo> shelftree;

    @ApiModelProperty(value = "仓库ID ")
    String warehouseid ;

}
