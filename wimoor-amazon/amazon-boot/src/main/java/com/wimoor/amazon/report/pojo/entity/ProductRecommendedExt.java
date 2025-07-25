package com.wimoor.amazon.report.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 

@Data
@TableName("t_product_recommended_ext")  
@ApiModel(value="ProductRecommendedExt对象", description="亚马逊产品推荐报表利润")
public class ProductRecommendedExt{
	@TableId(value=  "rid")
    private String rid;

	@TableField(value=  "marketplaceid")
    private String marketplaceid;

	@TableField(value=  "asin")
    private String asin;

	@TableField(value=  "imgurl")
    private String imgurl;

	@TableField(value=  "dim")
    private String dim;

	@TableField(value=  "currency")
    private String currency;
	
	@TableField(value=  "category")
    private String category;
	
	@TableField(value=  "price")
    private BigDecimal price;

	@TableField(value=  "margin")
    private BigDecimal margin;

	@TableField(value=  "profit")
    private BigDecimal profit;

	@TableField(value=  "refreshtime")
    private Date refreshtime;

  
	
	
	
    
}