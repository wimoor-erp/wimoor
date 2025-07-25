package com.wimoor.erp.stock.pojo.entity;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_inwh_form_entry")
public class InWarehouseFormEntry  extends BaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 433622908602012580L;

	@TableField(value= "formid")
    private String formid;

    @TableField(value= "materialid")
    private String materialid;

    @NotNull(message="数量不能为空")
    @TableField(value= "amount")
    private Integer amount;

 
}