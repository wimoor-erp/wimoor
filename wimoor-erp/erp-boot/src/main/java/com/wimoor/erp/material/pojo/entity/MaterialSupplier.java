package com.wimoor.erp.material.pojo.entity;

import java.math.BigDecimal;
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
@ApiModel(value="MaterialSupplier对象", description="产品供应商")
@TableName("t_erp_material_supplier")
public class MaterialSupplier extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6524217030268151614L;

	@ApiModelProperty(value = "产品ID")
	@TableField(value= "materialid")
    private String materialid;

	@ApiModelProperty(value = "供应商ID")
	@TableField(value= "supplierid")
    private String supplierid;

	@ApiModelProperty(value = "采购链接")
	@TableField(value= "purchaseUrl")
    private String purchaseurl;

	@ApiModelProperty(value = "产品编码")
	@TableField(value= "productCode")
    private String productcode;
	
	@ApiModelProperty(value = "产品编码")
	@TableField(value= "specId")
    private String specid;
	
	@ApiModelProperty(value = "其他开支")
	@TableField(value= "otherCost")
    private BigDecimal othercost;

	@ApiModelProperty(value = "发货周期")
	@TableField(value= "deliverycycle")
    private Integer deliverycycle;
	

	@ApiModelProperty(value = "是否默认供应商")
	@TableField(value= "isdefault")
    private Boolean isdefault;
	@ApiModelProperty(value = "创建人【系统填写】")
	@TableField(value= "creater")
    private String creater;

	@ApiModelProperty(value = "创建时间【系统填写】")
	@TableField(value= "createdate")
    private Date createdate;

	@ApiModelProperty(value = "修改人【系统填写】")
	@TableField(value= "operator")
    private String operator;

	@ApiModelProperty(value = "下单备注")
	@TableField(value= "remark")
    private String remark;
	
	@ApiModelProperty(value = "修改时间【系统填写】")
	@TableField(value= "opttime")
    private Date opttime;
	
	@ApiModelProperty(value = "不良率")
	@TableField(value= "badrate")
    private Float badrate;
	
	@TableField(value= "MOQ")
    private Integer MOQ;

 
}