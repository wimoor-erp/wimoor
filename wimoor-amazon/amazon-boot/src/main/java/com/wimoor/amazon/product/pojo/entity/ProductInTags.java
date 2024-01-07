package com.wimoor.amazon.product.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_in_tags")
@ApiModel(value="ProductInTags对象", description="产品标签对象")
public class ProductInTags implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -5469505744517586722L;
	
	@TableField(value = "pid")
	String pid;
	
	@TableField(value = "tagid")
	String tagid;
	
	@TableField(value = "operator")
	String operator;
	
	@TableField(value = "opttime")
	Date opttime; 
}
