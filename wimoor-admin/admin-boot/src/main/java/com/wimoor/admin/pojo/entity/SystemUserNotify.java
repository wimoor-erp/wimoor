package com.wimoor.admin.pojo.entity;

import java.util.Date;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

 
@Data
@Accessors(chain = true)
@TableName("t_sys_usernotify")
public class SystemUserNotify {

	@TableField(value="isRead")
    private Boolean isread;

	@TableField(value="userid")
    private String userid;

	@TableField(value="notify")
    private Integer notify;

	@TableField(value="createdAt")
    private Date createdat;
 
}