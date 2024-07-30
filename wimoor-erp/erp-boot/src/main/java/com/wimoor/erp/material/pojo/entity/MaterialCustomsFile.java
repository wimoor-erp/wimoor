package com.wimoor.erp.material.pojo.entity;

import java.math.BigInteger;
import java.util.Date;
 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MaterialCustomsFile对象", description="产品报关文件表")
@TableName("t_erp_material_customs_file")
public class MaterialCustomsFile extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3383893134923669184L;

	@ApiModelProperty(value = "产品ID")
	@TableField(value="materialid")
    private BigInteger materialid;

	@ApiModelProperty(value = "文件名称")
	@TableField(value="filename")
    private String filename;
	
	@ApiModelProperty(value = "文件路径")
	@TableField(value="filepath")
    private String filepath;

	@ApiModelProperty(value = "修改人【系统填写】")
	@TableField(value="operator")
    private BigInteger operator;

	@ApiModelProperty(value = "创建人【系统填写】")
	@TableField(value="creator")
    private BigInteger creator;

	@ApiModelProperty(value = "创建时间【系统填写】")
	@TableField(value="createtime")
    private Date createtime;

	@ApiModelProperty(value = "修改时间【系统填写】")
	@TableField(value="opttime")
    private Date opttime;

 
}