package com.wimoor.amazon.inboundV2.pojo.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ShipInboundShipmentDelivery对象", description="货件Item")
@TableName("t_erp_ship_v2_inboundshipment_delivery")
public class ShipInboundShipmentDelivery {
	@ApiModelProperty(value = "deliveryWindowOptionId")
	@TableId(value="delivery_window_option_id")
    private String deliveryWindowOptionId;
	
	@ApiModelProperty(value = "availabilityType")
	@TableField(value="availability_type")
    private String availabilityType;
	
	
	@ApiModelProperty(value = "editableUntil")
	@TableField(value="editable_until")
    private Date editableUntil;
	
	@ApiModelProperty(value = "endDate")
	@TableField(value="end_date")
    private Date endDate;
	
	@ApiModelProperty(value = "startDate")
	@TableField(value="start_date")
    private Date startDate;
}
