package com.wimoor.amazon.product.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="productStatus对象", description="产品销售状态")
@TableName("t_product_info_status_define") 
public class ProductInfoStatusDefine {
	 
	@TableId(type=IdType.AUTO)
	private String id;
	
	@TableField(value = "shopid")
    private String shopid;

	@TableField(value =  "Name")
    private String name;

	@TableField(value =  "issystem")
    private boolean issystem;
	
	@TableField(value =  "color")
    private String color;
	
	@TableField(value =  "opttime")
    private Date opttime;
	
	@TableField(value =  "createtime")
    private Date createtime;
	
	@TableField(value =  "operator")
    private String operator;
	
	@TableField(value =  "creator")
    private String creator;
	
	@TableField(value =  "remark")
    private String remark;
	
}
