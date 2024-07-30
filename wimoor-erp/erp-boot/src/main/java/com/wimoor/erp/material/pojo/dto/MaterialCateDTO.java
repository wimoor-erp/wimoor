package com.wimoor.erp.material.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MaterialCateDTO对象", description="material分类")
public class MaterialCateDTO extends BasePageQuery{
	
	String search;
}
