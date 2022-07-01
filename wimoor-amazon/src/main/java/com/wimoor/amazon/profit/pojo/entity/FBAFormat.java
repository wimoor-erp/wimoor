package com.wimoor.amazon.profit.pojo.entity;

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
@TableName("t_fbaformat")  
@ApiModel(value="FBAFormat对象", description="产品FBA费用")
public class FBAFormat extends BaseEntity {
	@TableField(exist=false)
	private static final long serialVersionUID = -4779753666412776010L;

	@TableField(exist=false)
	public static final String[] monthConversion = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
			"Oct", "Nov", "Dec" };

	@TableField(value= "country")
	private String country;

	@TableField(value= "ismedia")
	private Boolean ismedia;

	@TableField(value= "producttierId")
	private String producttierid;

	@TableField(value= "fba_format")
	private String fbaFormat;

	@TableField(value= "month")
	private String month;
	
	@TableField(value= "weight")
	private BigDecimal weight;

	@TableField(value= "effective_date")
	private Date effectiveDate;

	@TableField(value= "expiry_date")
	private Date expiryDate;

	@TableField(value= "dispatch_type")
	private String dispatchType;
 
}