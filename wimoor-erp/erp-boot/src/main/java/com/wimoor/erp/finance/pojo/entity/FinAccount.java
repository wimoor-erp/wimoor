package com.wimoor.erp.finance.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_fin_account")
public class FinAccount extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 124664298510526918L;

	@TableField(value="shopid")
    private String shopid;

	@TableField(value="createdate")
    private Date createdate;
	
	@TableField(value="balance")
    private BigDecimal balance;
	
	@TableField(value="paymeth")
    private Integer paymeth;
	
	@TableField(exist=false)
    private String paymethName;
	
	@TableField(value="isdefault")
    private Boolean isdefault;
	
	@TableField(value="isdelete")
    private Boolean isdelete;
	
	@TableField(value="name")
    private String name;
	
     
    
}