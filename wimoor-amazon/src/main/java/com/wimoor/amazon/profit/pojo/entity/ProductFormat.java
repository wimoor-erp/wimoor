package com.wimoor.amazon.profit.pojo.entity;
 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_productformat")  
@ApiModel(value="ProductFormat对象", description="产品尺寸")
public class ProductFormat extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7623590167612518803L;

	@TableField(value= "producttierId")
    private String producttierid;

	@TableField(value= "country")
    private String country;

	@TableField(value= "format")
    private String format;

	@TableField(value= "length_unit")
    private String lengthUnit;

	@TableField(value= "weight_unit")
    private String weightUnit;

	@TableField(value= "sort")
    private Integer sort;
 
}