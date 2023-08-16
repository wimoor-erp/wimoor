package com.wimoor.amazon.report.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_amz_product_active")  
@ApiModel(value="AmzProductActive对象", description="产品")
public class AmzProductActive  {
	@TableId(value= "sku")
    private String sku;

	@TableField(value= "asin")
    private String asin;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

	@TableField(value= "amazonauthid")
    private String amazonauthid;
	
	@TableField(value= "opendate")
    private Date opendate;

	@TableField(value= "price")
    private BigDecimal price;

 
}