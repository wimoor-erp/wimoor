package com.wimoor.erp.ship.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_transtype")
public class ErpShipTransType extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1813052749171545911L;

	@TableField(value= "shopid")
	private String shopid;

	@TableField(value= "name")
    private String name;

	@TableField(value= "operator")
    private String operator;

	@TableField(value= "opttime")
    private Date opttime;
	
	@TableField(value= "disable")
    private Boolean disable;
	
	

   
}