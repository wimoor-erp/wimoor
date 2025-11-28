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
@ApiModel(value="MaterialBrand对象", description="产品品牌")
@TableName("t_erp_material_brand")
public class MaterialBrand extends ErpBaseEntity{/**
	 * 
	 */
	private static final long serialVersionUID = -1014306496468147282L;
	
	@ApiModelProperty(value = "名称【长度不能超过100个字符】")
	@NotNull(message="名称不能为空")
	@Size(max=100,message="名称的长度不能超过100个字符")
	@TableField(value="name")
    private String name;
	
	@ApiModelProperty(value = "公司ID【系统填写】")
	@TableField(value="shopid")
	private String shopid;
	
	@ApiModelProperty(value = "备注【长度不能超过500个字符】")
	@Size(max=500,message="备注的长度不能超过500个字符")
	@TableField(value="remark")
    private String remark;

}
