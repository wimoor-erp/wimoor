package com.wimoor.amazon.profit.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_inplacefee")  
@ApiModel(value="t_inplacefee对象", description="")
public class InplaceFee {
	@TableField(value= "id")
    private String id;

	@TableField(value= "name")
    private String name;

	@TableField(value= "description")
    private String description;
	
	@TableField(value= "country")
    private String country;
 
}