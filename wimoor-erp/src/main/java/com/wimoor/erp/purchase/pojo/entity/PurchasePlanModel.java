package com.wimoor.erp.purchase.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName("t_erp_purchase_planmodel")
public class PurchasePlanModel {
	
    @TableId(value= "planid")
    private String planid;
 
    @TableField(value= "refreshtime")
    private Date refreshtime;
    
	@TableField(value= "modelid")
    private String modelid;
	
	@TableField(value= "isrun")
    private boolean isrun;
	
	@TableField(value="operator")
	private String opeartor;
 
    
    
}