package com.wimoor.amazon.report.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_product_recommended")  
@ApiModel(value="ProductRecommended对象", description="亚马逊产品推荐报表")
public class ProductRecommended  extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6588147240262976233L;

	@TableField(value=  "amazonAuthId")
    private String amazonauthid;

	@TableField(value=  "marketplaceid")
    private String marketplaceid;

	@TableField(value=  "refreshtime")
    private Date refreshtime;

	@TableField(value=  "asin")
    private String asin;

	@TableField(value=  "name")
    private String name;

	@TableField(value=  "link")
    private String link;

	@TableField(value=  "brand")
    private String brand;

	@TableField(value=  "category")
    private String category;

	@TableField(value=  "subcategory")
    private String subcategory;

	@TableField(value=  "lowestprice")
    private BigDecimal lowestprice;

	@TableField(value=  "fbaoffer")
    private Boolean fbaoffer;

	@TableField(value=  "amzoffer")
    private Boolean amzoffer;

	@TableField(value=  "offers")
    private Integer offers;

	@TableField(value=  "reviews")
    private Integer reviews;

	@TableField(value=  "rank")
    private Integer rank;

	@TableField(value=  "sales_rank_growth")
    private String salesRankGrowth;

	@TableField(value=  "page_views")
    private String pageViews;

	@TableField(value=  "manufacturer_part_number")
    private String manufacturerPartNumber;

	@TableField(value=  "EAN")
    private String ean;

	@TableField(value=  "UPC")
    private String upc;

	@TableField(value=  "model_number")
    private String modelNumber;

	@TableField(value=  "ISBN")
    private String isbn;

	@TableField(value=  "brandoffer")
    private Boolean brandoffer;

	@TableField(value=  "categoryoffer")
    private Boolean categoryoffer;

	@TableField(value=  "performance")
    private String performance;

	@TableField(value=  "istoprank")
    private Boolean istoprank;

	@TableField(value=  "islowprice")
    private Boolean islowprice;

	@TableField(value=  "onAmazon")
    private Boolean onamazon;
	
	@TableField(value=  "isrefresh")
    private Boolean isrefresh;

 
    
}