package com.wimoor.erp.material.pojo.entity;
 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MaterialCategory对象", description="产品分类")
@TableName("t_erp_material_category")
public class MaterialCategory extends ErpBaseEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -1472909250000311978L;
	@ApiModelProperty(value = "名称【长度不能超过100个字符】")
	@NotNull(message="名称不能为空")
	@Size(max=100,message="名称的长度不能超过100个字符")
	@TableField(value="name")
    private String name;

	@ApiModelProperty(value = "编码【长度不能超过50个字符，不能为空】")
	@NotNull(message="编码不能为空")
	@Size(max=50,message="编码的长度不能超过50个字符")
	@TableField(value="number")
    private String number;

	@ApiModelProperty(value = "颜色")
	@Size(max=10,message="颜色的长度不能超过10个字符")
	@TableField(value="color")
    private String color;
	
	@ApiModelProperty(value = "公司ID【系统填写】")
	@TableField(value="shopid")
	private String shopid;

	@ApiModelProperty(value = "备注【长度不能超过500个字符】")
	@Size(max=500,message="备注的长度不能超过500个字符")
	@TableField(value="remark")
    private String remark;

 
}