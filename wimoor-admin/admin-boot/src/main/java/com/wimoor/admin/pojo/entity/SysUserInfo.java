package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_userinfo")
public class SysUserInfo extends com.wimoor.common.pojo.entity.BaseEntity {
 
	private static final long serialVersionUID = -6529964872607791992L;
 
	@TableField(value= "name")
    private String name;

	@TableField(value= "sex")
    private Boolean sex;

	@TableField(value= "picture")
    private String picture;

	@TableField(value= "tel")
    private String tel;
	
	@TableField(value="company")
	private String company;
	
	@TableField(value="email")
	private String email;
	
	@TableField(value= "remark")
    private String remark;

	 
    
}