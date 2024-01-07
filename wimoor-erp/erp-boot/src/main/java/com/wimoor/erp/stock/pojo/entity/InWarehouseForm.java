package com.wimoor.erp.stock.pojo.entity;

 
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_inwh_form")
public class InWarehouseForm extends ErpBaseForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2139636352502090632L;

	@TableField(value= "warehouseid")
    private String warehouseid;
    
	@Size( max=500,message="备注的长度不能超过500个字符")
    @TableField(value= "remark")
    private String remark;
    
}