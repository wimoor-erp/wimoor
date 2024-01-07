package com.wimoor.erp.ship.pojo.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_ship_transchannel")
public class ShipTransChannel extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7457744067140904377L;

	@NotNull(message="名称不能为空")
	@Size(max=50,message="名称不能超过50个字符")
    @TableField(value= "name")
    private String name;
    
    @TableField(value= "shopid")
    private String shopid;
 
    
 
}