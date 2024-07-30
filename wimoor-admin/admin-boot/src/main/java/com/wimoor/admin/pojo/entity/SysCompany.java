package com.wimoor.admin.pojo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.admin.util.UUIDUtil;

import lombok.Data;

@Data
@TableName("t_shop") 
public class SysCompany implements Serializable{
 
private static final long serialVersionUID = -5797095751113022058L;

@TableId(value= "id")
String id;

@TableField(value= "name")
String name;

@TableField(value= "remark")
String remark;

@TableField(value= "invitecode")
String invitecode;

@TableField(value= "fromcode")
String fromcode;

@TableField(value= "boss_email")
String bossEmail;

public String getId() {
	if(id==null) {
		id=UUIDUtil.getUUIDshort();
	}
	return id;
}

public void setId(String id) {
	this.id = id;
}

}
