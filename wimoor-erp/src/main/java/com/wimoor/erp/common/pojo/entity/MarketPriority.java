package com.wimoor.erp.common.pojo.entity;

 
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_market_priority")
public class MarketPriority {
 
	@TableField(value= "marketplaceid")
    private String marketplaceid;
 
	@TableField(value= "groupid")
    private String groupid;

	@NotNull(message="等级不能为空")
	@TableField(value= "priority")
    private int priority;
	
    
}