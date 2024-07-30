package com.wimoor.amazon.finances.pojo.entity;

 
import java.util.Date;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_fin_settlement_formula")
@ApiModel(value="AmzFinSettlementFormula对象", description="")
public class AmzFinSettlementFormula extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9015592748663405662L;

	@TableField(value="shopid")
    private String shopid;

	@TableField(value="formula")
    private String formula;

	@TableField(value="field")
    private String field;

	@TableField(value="creator")
    private String creator;

	@TableField(value="operator")
    private String operator;

	@TableField(value="pricetype")
    private Integer pricetype;
	
	@TableField(value="createdate")
    private Date createdate;

	@TableField(value="opttime")
    private Date opttime;
  
    
    
}