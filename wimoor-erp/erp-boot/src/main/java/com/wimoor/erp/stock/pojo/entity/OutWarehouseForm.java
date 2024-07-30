package com.wimoor.erp.stock.pojo.entity;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_outwh_form")
public class OutWarehouseForm extends ErpBaseForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6409515507781739481L;

	@TableField(value= "warehouseid")
    private String warehouseid;
    
    @Size(max=100,message="名称不能超过100个字符")
    @TableField(value= "remark")
    private String remark;
    
    @Size(max=36,message="发货客户不能超过36个字符")
    @TableField(value= "purchaser")
    private String purchaser;
    
    @Size(max=500,message="发货地址不能超过500个字符")
    @TableField(value= "toaddress")
    private String toaddress;
    
    @Size(max=500,message="物流快递不能超过500个字符")
    @TableField(value= "express")
    private String express;
    
    @Size(max=50,message="快递编号不能超过50个字符")
    @TableField(value= "expressno")
    private String expressno;
    
    @Size(max=100,message="发货客户不能超过100个字符")
    @TableField(value= "customer")
    private String customer;
    
    @TableField(value= "ftype")
    private Integer ftype;
    
    

}