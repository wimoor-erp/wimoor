package com.wimoor.amazon.inboundV2.pojo.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
@ApiModel(value="ShipInboundPlanPlacementOptions对象", description="货件装箱详情")
@TableName("t_erp_ship_v2_inboundplan_placement_options")
public class ShipInboundPlanPlacementOptions {
	
	@ApiModelProperty(value = "PlacementOptionId")
	@NotNull(message="Placement_option_id不能为空")
    @TableField(value= "placement_option_id")
    private String placementOptionId;

	@ApiModelProperty(value = "平台inboundPlanId")
    @NotNull(message="inbound_plan_id不能为空")
    @TableField(value= "inbound_plan_id")
    private String inboundPlanId;
	
	@ApiModelProperty(value = "formid")
    @TableField(value= "formid")
    private String formid;
 
	@ApiModelProperty(value = "options状态")
    @TableField(value= "status")
    private String status;
	
	@ApiModelProperty(value = "options内容")
    @TableField(value= "contents")
    private String contents;
	
	@ApiModelProperty(value = "操作时间")
    @TableField(value= "opttime")
    private Date opttime;
	
	@ApiModelProperty(value = "操作人")
    @TableField(value= "operator")
    private String operator;
}