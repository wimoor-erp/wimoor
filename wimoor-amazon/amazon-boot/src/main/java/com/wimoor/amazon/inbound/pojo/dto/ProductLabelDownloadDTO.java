package com.wimoor.amazon.inbound.pojo.dto;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="ProductLabelDownloadDTO对象", description="标签下载")
public class ProductLabelDownloadDTO {
	@ApiModelProperty(value = "货件ID")
	 String shipmentid;
	@ApiModelProperty(value = "0提供SKU信息，1提供货件信息")
	 String ftype;
     List<Map<String,Object>> skuinfo;
}
