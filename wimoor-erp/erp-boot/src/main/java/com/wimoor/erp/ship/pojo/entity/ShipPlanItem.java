package com.wimoor.erp.ship.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipPlanItem对象", description="计划Item")
@TableName("t_erp_ship_planitem")
public class ShipPlanItem extends BaseEntity {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3347764075683658531L;

	@ApiModelProperty(value = "SKU")
	@TableField(value= "sku")
    private String sku;
	
	@ApiModelProperty(value = "计划子ID（每个计划每个国家是一个子计划")
    @TableField(value= "plansubid")
    private String plansubid;

	@ApiModelProperty(value = "状态")
    @TableField(value= "status")
    private Integer status;

	@ApiModelProperty(value = "本地产品ID")
    @TableField(value= "materialid")
    private String materialid;

	@ApiModelProperty(value = "发货量")
    @TableField(value= "amount")
    private Integer amount;
    
	@ApiModelProperty(value = "海外仓发货量")
    @TableField(value= "selfamount")
    private Integer selfamount;
    
	@ApiModelProperty(value = "需求发货量")
    @TableField(value= "needship")
    private Integer needship;
    
	@ApiModelProperty(value = "发货货值")
    @TableField(value= "goodsworth")
    private BigDecimal goodsworth;
    
	@ApiModelProperty(value = "发货重量")
    @TableField(value="planweight")
    private BigDecimal planweight;
    
	@ApiModelProperty(value = "发货体积")
    @TableField(value="dimweight")
    private BigDecimal dimweight;
    
	@ApiModelProperty(value = "站点ID")
    @TableField(exist = false)
    String marketplaceid;
 
    
}