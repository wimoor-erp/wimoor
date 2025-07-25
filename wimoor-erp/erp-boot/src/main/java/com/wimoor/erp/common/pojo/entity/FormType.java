package com.wimoor.erp.common.pojo.entity;

 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_formtype")
public class FormType {
	@TableId(value="id")
	private String id;
	
	@NotNull(message="名称不能为空")
	@Size(min=1,max=50,message="名称的长度不能超过50个字符")
	@TableField(value="name")
	private String name;
	
	@Size( max=500,message="备注的长度不能超过500个字符")
	@TableField(value="remark")
	private String remark;
	 

}
