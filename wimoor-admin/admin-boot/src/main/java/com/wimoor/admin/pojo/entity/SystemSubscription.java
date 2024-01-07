package com.wimoor.admin.pojo.entity;

import java.util.Date;

 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

 
@Data
@Accessors(chain = true)
@TableName("t_sys_subscription")
public class SystemSubscription {
	@TableField(value= "target")
    private String target;
	
	@TableField(value= "userid")
    private String userid;

	@TableField(value= "action")
    private String action;

	@TableField(value= "createdAt")
    private Date createdat;
    
	@TableField(value= "disable")
    private Boolean disable;
     
    
}