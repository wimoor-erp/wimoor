package com.wimoor.erp.purchase.pojo.entity;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_form_receive")
public class PurchaseFormReceive extends ErpBaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 8410503144873104397L;

	@TableField(value= "formentryid")
    private String formentryid;

    @TableField(value= "warehouseid")
    private String warehouseid;

    @TableField(value= "ftype")
    private String ftype;

    @NotNull(message="数量不能为空")
    @TableField(value= "amount")
    private Integer amount;
 
    @Size(max=200,message="备注不能超过200个字符")
    @TableField(value= "remark")
    private String remark;
 
    
  
 
}