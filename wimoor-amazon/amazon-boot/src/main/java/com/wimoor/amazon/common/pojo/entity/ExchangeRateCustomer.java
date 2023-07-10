package com.wimoor.amazon.common.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_exchangerate_customer")  
@ApiModel(value="ExchangeRateCustomer对象", description="ExchangeRateCustomer")
public class ExchangeRateCustomer extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5968023350465916717L;

	@TableField(value= "shopid")
    private String shopid;

    @TableField(value= "name")
    private String name;

    @TableField(value= "price")
    private BigDecimal price;

    @TableField(value= "symbol")
    private String symbol;

    @TableField(value= "type")
    private String type;

    @TableField(value= "utctime")
    private Date utctime;

    @TableField(value= "operator")
    private String operator;
 

  
}