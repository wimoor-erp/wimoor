package com.wimoor.erp.material.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_material_tags")
@ApiModel(value="MaterialTags对象", description="产品标签对象")
public class MaterialTags implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -5469505744517586722L;
	
	@TableField(value = "mid")
	String mid;
	
	@TableField(value = "tagid")
	String tagid;
	
	@TableField(value = "operator")
	String operator;
	
	@TableField(value = "opttime")
	Date opttime; 
}
