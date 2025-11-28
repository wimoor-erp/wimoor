package com.wimoor.erp.stock.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_changewh_form_entry")
public class ChangeWhFormEntry extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1033304113895130323L;

	@TableField(value= "formid")
    private String formid;

	@TableField(value= "material_from")
    private String materialFrom;

	@TableField(value= "material_to")
    private String materialTo;

	@TableField(value= "amount")
    private Integer amount;

 
}