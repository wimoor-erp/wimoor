package com.wimoor.erp.purchase.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_fin_form_payment")
public class PurchaseFinanceFormPayment extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = -1714290852154032614L;

	@TableField(value="formid")
	private String formid;
	
	@TableField(value="formentryid")
	private String formentryid;
	
	@TableField(value="acct")
	private String acct;
	
	@TableField(value= "payprice")
    private BigDecimal payprice;
	
	@TableField(value= "opttime")
    private Date opttime;
	
	@TableField(value= "createdate")
    private Date createdate;
	
	@TableField(value="remark")
	private String remark;
	
	@TableField(value="operator")
	private String operator;
	
	@TableField(value="projectid")
	private String projectid;
	
	@TableField(exist=false)
	private String projectname;
}
