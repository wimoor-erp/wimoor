package com.wimoor.erp.assembly.pojo.entity;


 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_assembly_from_instock")
public class AssemblyEntryInstock extends ErpBaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3952616210254778206L;

	@TableField(value="formid")
    private String formid;

	@NotNull(message="数量不能为空")
	@TableField(value="amount")
    private Integer amount;

	@Size(max=500,message="备注的长度不能超过500个字符")
	@TableField(value="remark")
    private String remark;
 
	@TableField(value="warehouseid")
    private String warehouseid;
	
	@TableField(value="shipmentid")
    private String shipmentid;
	
 
}