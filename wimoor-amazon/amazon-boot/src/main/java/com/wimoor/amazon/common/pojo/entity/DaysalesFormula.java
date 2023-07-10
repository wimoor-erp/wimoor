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
@TableName("t_daysales_formula")
@ApiModel(value="daysalesFormula对象", description="DaysalesFormula")
public class DaysalesFormula extends BaseEntity{
	

	private static final long serialVersionUID = -7366954230092708886L;

	@TableField(value = "shopid")
    private String shopid;
	
	@TableField(value = "formula")
    private String formula;
	
	
	@TableField(value = "formula_name")
    private String formulaName;
	
	@TableField(value = "month_sales_rate")
    private BigDecimal monthSalesRate;

	@TableField(value = "15sales_rate")
    private BigDecimal salesRate15;

	@TableField(value = "7sales_rate")
    private BigDecimal salesRate7;

	@TableField(value = "creator")
    private String creator;

	@TableField(value = "operator")
    private String operator;

	@TableField(value = "createdate")
    private Date createdate;

	@TableField(value = "opttime")
    private Date opttime;

    
}