package com.wimoor.amazon.inboundV2.pojo.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SKUItemDTO {


    @ApiModelProperty(value = "产品SKU ")
    String sku;
    @ApiModelProperty(value = "产品名称")
    String name;
    @ApiModelProperty(value = "发货数量")
    Integer quantity;
    @ApiModelProperty(value = "长")
    BigDecimal length;
    @ApiModelProperty(value = "宽")
    BigDecimal width;
    @ApiModelProperty(value = "高")
    BigDecimal height;
    @ApiModelProperty(value = "重")
    BigDecimal weight;
    @ApiModelProperty(value = "预计箱子数")
    Integer boxnumber;
    @ApiModelProperty(value = "每箱个数")
    Integer boxquantity;
    boolean isused=false;
    List<Integer> quantities;
    List<Integer> boxnums;
}
