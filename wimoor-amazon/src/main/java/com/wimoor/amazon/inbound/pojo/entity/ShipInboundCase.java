package com.wimoor.amazon.inbound.pojo.entity;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundCase对象", description="货件装箱详情")
@TableName("t_erp_ship_inboundcase")
public class ShipInboundCase extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7808691102390409776L;
	@ApiModelProperty(value = "货件号")
	@NotNull(message="ShipmentId不能为空")
    @TableField(value= "shipmentid")
    private String shipmentid;

	@ApiModelProperty(value = "平台SKU")
    @NotNull(message="SKU不能为空")
    @TableField(value= "merchantsku")
    private String merchantsku;

	@ApiModelProperty(value = "混装发货永远为1,仅用于原装发货")
    @TableField(value= "unitspercase")
    private Integer unitspercase;

	@ApiModelProperty(value = "箱号")
    @TableField(value= "numberofcase")
    private Integer numberofcase;

	@ApiModelProperty(value = "箱子内对应SKU的数量")
    @TableField(value= "quantity")
    private Integer quantity;
 
}