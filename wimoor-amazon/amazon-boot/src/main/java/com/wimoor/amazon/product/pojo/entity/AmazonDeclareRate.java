package com.wimoor.amazon.product.pojo.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amazon_declare_rate")
@ApiModel(value="AmazonDeclareRate对象", description="")
public class AmazonDeclareRate {

	
	@TableId(value = "shopid" )
    String shopid;
	
	
	@TableField(value = "rate" )
    Float rate;
	
}
