package com.wimoor.amazon.product.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductFnSKUListDTO对象", description="标签打印查询")
public class ProductFnSKUListDTO  extends BasePageQuery{
	String groupid;
	String marketplaceid;
	String ftype;
	String search;
}
