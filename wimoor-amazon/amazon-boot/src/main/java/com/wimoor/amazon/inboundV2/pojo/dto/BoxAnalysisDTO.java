package com.wimoor.amazon.inboundV2.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value=" BoxAnalysisDTO对象", description="分析箱子使用")
public class BoxAnalysisDTO {


    @ApiModelProperty(value = "产品SKU列表 ")
    List<SKUItemDTO> itemlist;
    @ApiModelProperty(value = "箱子列表 ")
    List<BoxInfo> boxlist;
    String type;
}
