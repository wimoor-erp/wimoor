package com.wimoor.erp.ship.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_planmodel")
public class ShipPlanModel  extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4901853009579545032L;

	@TableField(value="planid")
    private String planid;

	@TableField(value="warehouseid")
    private String warehouseid;

	@TableField(value="groupid")
    private String groupid;

	@TableField(value="refreshtime")
    private Date refreshtime;
	
	@TableField(value="isrun")
    private Boolean isrun;
	
	@TableField(value="operator")
	private String operator;

 
}