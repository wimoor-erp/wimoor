package com.wimoor.erp.material.pojo.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@TableName("t_erp_material_mark")
@ApiModel(value="MaterialMark对象", description="产品备注用于补发货规划")
public class MaterialMark   {
	public final static String Type_Notice="notice";
	public final static String Type_Hide="phide";
	@Size(max=100,message="信息不能超过100个字符")
	@TableField(value =  "mark")
    private String mark;
	
	@ApiModelProperty(value = "修改人【系统填写】")
	@TableField(value =  "operator")
    private String operator;
    
	@ApiModelProperty(value = "修改时间【系统填写】")
	@TableField(value =  "opttime")
    private Date opttime;

	@ApiModelProperty(value = "产品ID[必填]")
	@TableField(value =  "materialid")
    private String materialid;

	@ApiModelProperty(value = "备注类型")
	@TableField(value = "ftype")
    private String ftype;
    
	@TableField(exist=false)
	List<MaterialMarkHis> hisList;
    
}