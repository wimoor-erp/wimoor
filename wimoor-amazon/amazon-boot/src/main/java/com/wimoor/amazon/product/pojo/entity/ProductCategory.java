package com.wimoor.amazon.product.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="Material对象", description="产品物流对象")
@TableName("t_product_category") 
public class ProductCategory {
	
	@TableId(value = "CategoryId")
    private String categoryid;
	
	@TableField(value = "pid")
    private String pid;

	@TableField(value =  "Name")
    private String name;

	@TableField(value =  "parentId")
    private String parentid;

}