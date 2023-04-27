package com.wimoor.amazon.auth.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_warehouse_fba")
public class WareHouseFBA {
	@TableId(value= "id")
    private BigInteger id;
	
	@TableField(value= "shopid")
    private BigInteger shopid;
	
	@TableField(value= "marketplaceid")
    private String marketplaceid;
	
	@TableField(value= "remark")
    private String remark;
	
	@TableField(value= "name")
    private String name;
	
	@TableField(value= "number")
    private String number;

	@TableField(value= "stocking_cycle")
    private Integer stockingCycle;

	@TableField(value= "min_cycle")
    private Integer minCycle;

	@TableField(value= "put_on_days")
    private Integer putOnDays;

	@TableField(value= "first_leg_days")
    private Integer firstLegDays;

	@TableField(value= "operator")
    private String operator;

	@TableField(value= "opttime")
    private Date opttime;
 
}