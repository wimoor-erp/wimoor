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
@TableName("t_fba_labeling_service_fee")  
@ApiModel(value="FBALabelingFee对象", description="产品Label费用")
public class FBALabelingFee extends BaseEntity {
	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = 7534998846946153980L;

	@TableField(value= "isStandard")
    private Boolean isStandard;

	@TableField(value= "price")
    private BigDecimal price;

	@TableField(value= "country")
    private String country;
 
}