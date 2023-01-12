package com.wimoor.erp.assembly.pojo.entity;

 
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_assembly")
public class Assembly extends ErpBaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 637203926176247966L;

	@TableField(value= "mainmid")
    private String mainmid;

    @TableField(value= "submid")
    private String submid;

    @TableField(value= "subnumber")
    private Integer subnumber;

    @Size(max=200,message="其它联系信息的长度不能超过200个字符")
    @TableField(value= "remark")
    private String remark;
 
 
}