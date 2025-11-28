package com.wimoor.amazon.inboundV2.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="ShipLabelDto对象", description="货件打标")
public class ShipLabelDto {
    String  sku;
    Integer amount;
    String  groupid;
    String  marketplaceid;
    String  amazonauthid;
    String  number;
}
