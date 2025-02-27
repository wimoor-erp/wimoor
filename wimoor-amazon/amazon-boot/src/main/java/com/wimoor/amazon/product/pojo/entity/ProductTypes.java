package com.wimoor.amazon.product.pojo.entity;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_type")
@ApiModel(value="ProductType对象", description="产品类型信息")
public class ProductTypes  extends BaseEntity{
 

    /**
	 * 
	 */
	private static final long serialVersionUID = -647508122376298745L;

	/**
	 * 
	 */
	 

	private String name;

    private Date refreshtime;
}