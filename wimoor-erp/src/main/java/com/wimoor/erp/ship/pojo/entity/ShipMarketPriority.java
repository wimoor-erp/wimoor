package com.wimoor.erp.ship.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_marketpriority")
public class ShipMarketPriority extends ErpBaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3664990564340613519L;

	@TableField(value= "shopid")
    private String shopid;

    @TableField(value= "marketplaceid")
    private String marketplaceid;

    @TableField(value= "region")
    private String region;

    @TableField(value= "level")
    private Integer level;

  
 
}