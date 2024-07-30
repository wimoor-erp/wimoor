package com.wimoor.amazon.profit.pojo.entity;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_inplacefeeformat")  
@ApiModel(value="InplaceFeeFormat对象", description="产品Inplace费用")
public class InplaceFeeFormat implements Serializable{

	@TableField(exist=false)
	private static final long serialVersionUID = -7509423765425122190L;
   
	@TableId(value= "id")
    private Integer id;

	@TableField(value= "inplacefeeid")
    private String inplacefeeid;
	
	@TableField(value= "producttierId")
    private String producttierId;

	@TableField(value= "standard")
    private Boolean standard;

	@TableField(value= "format")
    private String format;

	@TableField(value= "description")
    private String description;

	@TableField(value= "country")
    private String country;
}