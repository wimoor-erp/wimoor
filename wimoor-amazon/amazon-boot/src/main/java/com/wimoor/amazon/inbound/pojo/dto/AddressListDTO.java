package com.wimoor.amazon.inbound.pojo.dto;



import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AddressListDTO对象", description="货件")
public class AddressListDTO extends BasePageQuery{
	@ApiModelProperty("店铺ID")
    String groupid;
	@ApiModelProperty("是否删除")
	String isdisable;
	@ApiModelProperty("查询名称")
	String search;
}
