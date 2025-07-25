package com.wimoor.amazon.product.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProductFollow对象", description="商品更买")
@TableName("t_product_follow") 
public class ProductFollow extends BaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 251004025361783061L;

	@TableField(value = "asin")
    private String asin;

    @TableField(value = "sku")
    private String sku;
    
    @TableField(value = "amazonAuthId")
    private String amazonAuthId;
    
    @TableField(value = "marketplaceid")
    private String marketplaceid;
    
    @TableField(value = "lastupdateTime")
    private Date lastupdateTime;

    @TableField(value = "isread")
    private Boolean isread;

    @TableField(value = "flownumber")
    private Integer flownumber;
     

 
}