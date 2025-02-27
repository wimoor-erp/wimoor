package com.wimoor.erp.purchase.pojo.entity;


import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_form_payment")
public class PurchaseFormPayment extends ErpBaseEntity{
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 8737105380453284910L;

	@TableField(value= "formentryid")
    private String formentryid;

    @TableField(value= "projectid")
    private String projectid;
    
    @NotNull(message="付款金额不能为空")
    @TableField(value= "payprice")
    private BigDecimal payprice;
    
    @TableField(value= "remark")
    private String remark;
    
    @TableField(value= "createdate")
    private Date createdate;
    
    @TableField(value= "acct")
    private String acct;
    
    @TableField(value= "auditstatus")
    private Integer auditstatus;
    
    @TableField(value= "payment_method")
    private Integer paymentMethod;
    
    @TableField(exist=false)
    private String methodname;
    
    @TableField(exist=false)
    private String projectname;
     
	
    
	 
 
}