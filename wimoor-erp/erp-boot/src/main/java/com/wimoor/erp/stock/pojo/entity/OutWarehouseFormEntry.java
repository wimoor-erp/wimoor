package com.wimoor.erp.stock.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_outwh_form_entry")
public class OutWarehouseFormEntry  extends BaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -4462499864536230826L;

	@TableField(value= "formid")
    private String formid;

    @TableField(value= "materialid")
    private String materialid;
    


    @TableField(value= "amount")
    private Integer amount;

 
}