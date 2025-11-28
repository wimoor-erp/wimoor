package com.wimoor.amazon.profit.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fba_fbasipp")  
@ApiModel(value="FBAFormat对象", description="产品FBA费用")
public class FBASipp extends BaseEntity {
	@TableField(exist=false)
	private static final long serialVersionUID = -4779753666412776010L;

	@TableField(value= "country")
	private String country;
	
	@TableField(value= "producttierId")
	private String producttierid;

	@TableField(value= "format")
	private String format;
	
	@TableField(value= "weight")
	private BigDecimal weight;
 
	@TableField(value= "isclothing")
	private Boolean isclothing;
}