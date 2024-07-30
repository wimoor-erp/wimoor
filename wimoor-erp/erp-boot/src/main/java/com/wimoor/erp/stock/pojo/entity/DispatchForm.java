package com.wimoor.erp.stock.pojo.entity;

import java.util.Date;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_dispatch_form")
public class DispatchForm extends ErpBaseForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1695655170767865651L;

	@TableField(value= "from_warehouseid")
    private String fromWarehouseid;

	@TableField(value= "to_warehouseid")
    private String toWarehouseid;

	@Size( max=500,message="备注的长度不能超过500个字符")
	@TableField(value= "remark")
    private String remark;
 
	@TableField(value= "ftype")
	private Integer ftype;
	
    @TableField(value="arrivalTime")
	private Date arrivalTime;
    
  

}