package com.wimoor.amazon.profit.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_outbound_weightformat")  
@ApiModel(value="OutboundWeightFormat对象", description="产品OutboundWeight费用")
public class OutboundWeightFormat extends BaseEntity {
	/**
	 * 
	 */
	@TableField(exist=false)
	private static final long serialVersionUID = -8317914376471873799L;

	@TableField(value= "producttierId")
    private String producttierid;

	@TableField(value= "isMedia")
    private Boolean ismedia;

	@TableField(value= "format")
    private String format;
 
}