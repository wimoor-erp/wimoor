package com.wimoor.amazon.inbound.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.AmazonBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundBox对象", description="货件箱子信息")
@TableName("t_erp_ship_inboundbox")
public class ShipInboundBox extends AmazonBaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2429906861856659950L;
	@ApiModelProperty(value = "货件号")
	@TableField(value= "shipmentid")
    private String shipmentid;
	
	@ApiModelProperty(value = "箱号")
    @TableField(value= "boxnum")
    private Integer boxnum;

	@ApiModelProperty(value = "箱子长度")
    @TableField(value= "length")
    private BigDecimal length;

	@ApiModelProperty(value = "箱子宽度")
    @TableField(value= "width")
    private BigDecimal width;

	@ApiModelProperty(value = "箱子高度")
    @TableField(value= "height")
    private BigDecimal height;

	@ApiModelProperty(value = "高宽高单位")
    @TableField(value= "unit")
    private String unit;

	@ApiModelProperty(value = "重量")
    @TableField(value= "weight")
    private BigDecimal weight;

	@ApiModelProperty(value = "重量单位")
    @TableField(value= "wunit")
    private String wunit;
    
	@ApiModelProperty(value = "物流追踪编号")
    @TableField(value= "tracking_id")
    private String trackingId;
    
	@ApiModelProperty(value = "物流追踪状态【系统填写】")
    @TableField(value= "package_status")
    private String packageStatus;
    
	@ApiModelProperty(value = "SKU数量【系统填写】")
    @TableField(exist = false)
    private Integer sumsku;
    
     
}