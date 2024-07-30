package com.wimoor.erp.assembly.pojo.entity;

 
 
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 


@Data
@EqualsAndHashCode(callSuper = true)
@TableName( "t_erp_assembly_form_entry")
public class AssemblyFormEntry extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5899258152791104778L;

	@TableField(value= "warehouseid")
	private String warehouseid;

    @NotNull(message="数量不能为空")
    @TableField(value= "amount")
	private Integer amount;

    @TableField(value= "whamount")
	private Integer whamount;

    @TableField(value= "phamount")
	private Integer phamount;
    
    @TableField(value= "phedamount")
   	private Integer phedamount;
    
    @TableField(value= "subnumber")
   	private Integer subnumber;
    
    @TableField(value= "purchase_from_entry_id")
	private String purchaseFromEntryId;
    
 
    @TableField(value= "formid")
	private String formid;

  
    @TableField(value= "materialid")
	private String materialid;
 
	 
	

}
