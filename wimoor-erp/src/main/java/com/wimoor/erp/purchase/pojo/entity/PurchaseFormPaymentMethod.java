package com.wimoor.erp.purchase.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_purchase_form_payment_method")
public class PurchaseFormPaymentMethod {

	 @TableId(value= "id")
	 private Integer id;
	 
	 @TableField(value= "name")
	 private String name;
	 
	 @TableField(value= "opttime")
	 private Date opttime;
	 
	 @TableField(value= "createtime")
	 private Date createtime;
	 
	 @TableField(value= "operator")
	 private String operator;
	 
	 @TableField(value= "creator")
	 private String creator;

	 
	 
	
}
