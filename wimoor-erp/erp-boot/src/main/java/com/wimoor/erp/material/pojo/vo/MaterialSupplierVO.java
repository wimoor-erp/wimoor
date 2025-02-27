package com.wimoor.erp.material.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.wimoor.erp.material.pojo.entity.MaterialSupplierStepwise;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MaterialSupplierVO对象", description="产品供应商对应关系")
public class MaterialSupplierVO {
	@ApiModelProperty(value = "本地产品ID")
	String id;
	
	@ApiModelProperty(value = "本地产品ID")
	String materialid;
	
	@ApiModelProperty(value = "供应商ID")
	String supplierid;
	
	@ApiModelProperty(value = "供应商类型")
	String ftype;
	
	@ApiModelProperty(value = "采购链接")
	String purchaseUrl;
	
	@ApiModelProperty(value = "采购编码")
	String productCode;
	
	@ApiModelProperty(value = "其他采购成本")
	BigDecimal otherCost;
	
	@ApiModelProperty(value = "供货周期[单位:天]")
	Integer deliverycycle;
	
	@ApiModelProperty(value = "是否默认")
	Boolean isdefault;
	
	@ApiModelProperty(value = "不良率")
	BigDecimal badrate;
	
	@ApiModelProperty(value = "起订量")
	Integer MOQ;
	
	@ApiModelProperty(value = "创建时间")
	Date createdate;
	
	@ApiModelProperty(value = "操作时间")
	Date opttime;
	
	@ApiModelProperty(value = "操作人")
	String username;
	
	@ApiModelProperty(value = "供应商名称")
	String name;
	
	@ApiModelProperty(value = "供应商名称")
	String remark;
	
	@ApiModelProperty(value = "供应编码")
	String number;
	
	@ApiModelProperty(value = "阶梯价List")
	List<MaterialSupplierStepwise> stepList;
	
	
	
}
