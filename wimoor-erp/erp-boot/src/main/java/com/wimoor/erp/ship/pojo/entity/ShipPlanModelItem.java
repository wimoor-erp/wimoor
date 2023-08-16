package com.wimoor.erp.ship.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_planmodelitem")
public class ShipPlanModelItem extends BaseEntity {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1820023010602020388L;

	@TableField(value="modelid")
    private String modelid;

	@TableField(value="materialid")
    private String materialid;

	@TableField(value="planamount")
    private Integer planamount;
}