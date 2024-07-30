package com.wimoor.erp.material.pojo.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_m_group")
public class ERPGroup extends BaseEntity{
	
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -6732119525329165246L;


	@TableField(value= "shopid")
    private String shopid;

    
    @TableField(value= "ftype")
    private Integer ftype;

    @NotNull(message="颜色不能为空")
	@Size(min=1,max=10,message="颜色的长度不能超过10个字符")
    @TableField(value= "color")
    private String color;

    @TableField(value= "issys")
    private Boolean issys;

    @NotNull(message="名称不能为空")
	@Size(min=1,max=50,message="名称的长度不能超过40个字符")
    @TableField(value= "name")
    private String name;

   	@Size( max=500,message="备注的长度不能超过500个字符")
    @TableField(value= "remark")
    private String remark;

    @TableField(value= "opttime")
    private Date opttime;

    @TableField(value= "operator")
    private String operator;

    @TableField(exist = false)
    boolean checked=false;
    
     
}