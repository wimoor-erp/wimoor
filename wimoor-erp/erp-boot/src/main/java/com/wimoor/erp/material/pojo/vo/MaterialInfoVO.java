package com.wimoor.erp.material.pojo.vo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wimoor.erp.assembly.pojo.vo.AssemblyVO;
import com.wimoor.erp.inventory.pojo.entity.Inventory;
import com.wimoor.erp.material.pojo.entity.DimensionsInfo;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.material.pojo.entity.MaterialCustomsItem;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MaterialInfoVO对象", description="产品详情")
public class MaterialInfoVO {

	@ApiModelProperty(value = "产品基础数据")
	MaterialVO material;
	
	@ApiModelProperty(value = "产品item尺寸重量对象")
	DimensionsInfo itemDim;
	
	@ApiModelProperty(value = "产品box尺寸重量对象")
	DimensionsInfo boxDim;
	
	@ApiModelProperty(value = "产品pkg尺寸重量对象")
	DimensionsInfo pkgDim;
	
	@ApiModelProperty(value = "产品阶梯价格")
	List<StepWisePrice> stepWisePrice;
	
	@ApiModelProperty(value = "产品组装列表")
	List<AssemblyVO> assemblyList;
	
	@ApiModelProperty(value = "产品耗材列表")
	List<MaterialConsumableVO> consumableList;
	
	@ApiModelProperty(value = "产品耗材列表")
	List<MaterialConsumableVO> consumablePList;
	
	@ApiModelProperty(value = "产品供应商列表")
	List<MaterialSupplierVO> supplierList;
	
	@ApiModelProperty(value = "产品海关基础数据")
	MaterialCustoms customs;
	
	@ApiModelProperty(value = "可用库存")
	Inventory fulfillable;
	
	@ApiModelProperty(value = "可组装库存")
	Integer canAssembly;
	
	@ApiModelProperty(value = "产品海关列表数据")
	List<MaterialCustomsItem> customsItemList;
	
	@ApiModelProperty(value = "产品父亲列表")
	List<Map<String, Object>> parentList;
	
	@ApiModelProperty(value = "产品标签列表数据")
	String taglist;
	
	@ApiModelProperty(value = "产品标签列表名称数据")
	List<Map<String,Object>> tagNameList;
	
	@ApiModelProperty(value = "是否复制")
	String iscopy;
	
	Object imgfile;
	
	@ApiModelProperty(value = "是否归档")
    @TableField(value = "isDelete")
    private Boolean isDelete;
}
