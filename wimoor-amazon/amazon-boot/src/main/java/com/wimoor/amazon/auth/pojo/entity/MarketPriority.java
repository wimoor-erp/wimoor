package com.wimoor.amazon.auth.pojo.entity;

 
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
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
	
	@TableField(value =  "opttime")
	private Date opttime;
    
	@ApiModelProperty(value = "操作人ID【系统填写】")
    @TableField(value =  "operator")
    private String operator;
}