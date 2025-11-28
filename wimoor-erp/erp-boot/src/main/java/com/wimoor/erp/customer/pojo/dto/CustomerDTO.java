package com.wimoor.erp.customer.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="CustomerDTO对象", description="供应商")
public class CustomerDTO extends BasePageQuery{

	String search;
	String materialid;
}
