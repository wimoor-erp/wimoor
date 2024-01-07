package com.wimoor.erp.purchase.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_fin_form")
public class PurchaseFinanceForm extends BaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = -2629744904924193826L;

	@TableField(value="entryid")
	private String entryid;
	
	@TableField(value="shopid")
	private String shopid;
	
	@TableField(value="auditstatus")
	private Integer auditstatus;
	
	@TableField(value="payment_method")
	private Integer paymentMethod;
	
	@TableField(value="number")
	private String number;
	
	@TableField(value="remark")
	private String remark;
	
	@TableField(value="audittime")
	private Date audittime;
	
	@TableField(value="opttime")
	private Date opttime;
	
	@TableField(value="createtime")
	private Date createtime;
	
	@TableField(value="auditor")
	private String auditor;
	
	@TableField(value="operator")
	private String operator;
	
	@TableField(value="creator")
	private String creator;
}
