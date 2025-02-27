package com.wimoor.erp.material.pojo.entity;

import java.util.Date;
 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="MaterialGroup对象", description="产品分组")
@TableName("t_erp_material_group")
public class MaterialGroup  {
 
	@ApiModelProperty(value = "产品ID")
	@TableField(value = "materialid")
    private String materialid;

	@ApiModelProperty(value = "分组ID")
	@TableField(value = "groupid")
    private String groupid;
    
	@ApiModelProperty(value = "修改时间【系统填写】")
    @TableField(value="opttime")
    Date opttime ;
    
	@ApiModelProperty(value = "修改人【系统填写】")
    @TableField(value="operator")
    String operator;
 
    
}
