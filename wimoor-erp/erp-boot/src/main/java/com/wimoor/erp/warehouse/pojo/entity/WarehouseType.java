package com.wimoor.erp.warehouse.pojo.entity;


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
@ApiModel(value="WarehouseType对象", description="仓库类型")
@TableName("t_erp_warehouse_type")
public class WarehouseType  extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6663429723836367575L;
    @ApiModelProperty(value = "公司ID")
	@TableField(value= "shopid")
    private String shopid;
	 
    @ApiModelProperty(value = "是否系统")
    @TableField(value= "issystem")
    private Boolean issystem;

    @ApiModelProperty(value = "名称")
    @NotNull(message="名称不能为空")
    @TableField(value= "name")
    private String name;

    @ApiModelProperty(value = "备注")
	@Size(max=500,message="备注不能超过500个字符")
    @TableField(value= "remark")
    private String remark;
 
 
}