package com.wimoor.erp.stock.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundBox对象", description="货件箱子信息")
@TableName("t_erp_dispatch_oversea_box")
public class ErpDispatchOverseaBox extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2429906861856659950L;
	@ApiModelProperty(value = "计划ID")
	@TableField(value= "formid")
    private String formid;

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

 	@ApiModelProperty(value = "装箱详情")
 	@TableField(exist = false)
    List<ErpDispatchOverseaCase> caseListDetail;
     
}