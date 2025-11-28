package com.wimoor.admin.pojo.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
 
@Data
@Accessors(chain = true)
@TableName("t_sys_notify")
public class SystemNotify {
	@TableId(value = "id" )
    private Integer id;

	@TableField(value= "title")
	private String title;
	
	@TableField(value= "content")
    private String content;

	@TableField(value= "ftype")
    private Integer type;

	@TableField(value= "target")
    private String target;

	@TableField(value= "action")
    private String action;

	@TableField(value= "sender")
    private String sender;
	
	@TableField(value= "receiver")
	private String receiver;

	@TableField(value= "createdAt")
    private Date createdat;

	@TableField(value= "shopid")
    private String shopid;
	
	@TableField(exist= false)
    private Boolean isRead;
}