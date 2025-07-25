package com.wimoor.amazon.inboundV2.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value=" BoxList对象BoxInfo", description="箱子列表item")
public class BoxInfo {


    @ApiModelProperty(value = "长")
    BigDecimal length;
    @ApiModelProperty(value = "宽")
    BigDecimal width;
    @ApiModelProperty(value = "高")
    BigDecimal height;

    @ApiModelProperty(value = "占用体积")
    BigDecimal skuTotalVolumn;

    @ApiModelProperty(value = "占用重量")
    BigDecimal  skuTotalWeight;

    @ApiModelProperty(value = "使用频率")
    Integer num;

    boolean isok;

    @ApiModelProperty(value = "skuitemDTO")
    List<SKUItemDTO> itemlist;
}
