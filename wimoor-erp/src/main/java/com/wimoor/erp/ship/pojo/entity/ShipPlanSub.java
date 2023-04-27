package com.wimoor.erp.ship.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_plansub")
public class ShipPlanSub  extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5995807965950372330L;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

    @TableField(value= "planid")
    private String planid;

    @TableField(value= "status")
    private Integer status;

    @TableField(value= "opttime")
    private Date opttime;
     
}