package com.wimoor.amazon.common.pojo.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_importrecord")
public class ImportRecord extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1778696023432811443L;
	
	@TableField(value= "opttime")
	private Date opttime;
	
	@TableField(value= "operator")
	private String operator;
	
	@TableField(value= "shopid")
	private String  shopid;
	
	@TableField(value= "issuccess")
	private Boolean issuccess;
	
	@TableField(value= "importtype")
	private String importtype;
	
	@TableField(value= "log")
	private String log;
	
	
}
