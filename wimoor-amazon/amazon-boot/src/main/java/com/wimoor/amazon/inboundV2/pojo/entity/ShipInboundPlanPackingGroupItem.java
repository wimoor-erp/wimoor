package com.wimoor.amazon.inboundV2.pojo.entity;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
@ApiModel(value="ShipInboundPlanPackingGroupItem对象", description="发后计划分组ITEM")
@TableName("t_erp_ship_v2_inboundplan_packing_options_group_item")
public class ShipInboundPlanPackingGroupItem {
 
	@ApiModelProperty(value = "packingOptionId")
	@NotNull(message="packing_option_id不能为空")
    @TableField(value= "packing_group_id")
    private String packingGroupId;
 
	
	@ApiModelProperty(value = "packingOptionId")
	@NotNull(message="packing_option_id不能为空")
    @TableField(value= "packing_option_id")
    private String packingOptionId;
	
	@ApiModelProperty(value = "平台inboundPlanId")
    @NotNull(message="inbound_plan_id不能为空")
    @TableField(value= "inbound_plan_id")
    private String inboundPlanId;
	
	
	@ApiModelProperty(value = "formid")
    @TableField(value= "formid")
    private String formid;
	
	@ApiModelProperty(value = "sku")
    @TableField(value= "sku")
    private String sku;
	
	@ApiModelProperty(value = "quantity")
    @TableField(value= "quantity")
    private Integer quantity;
}