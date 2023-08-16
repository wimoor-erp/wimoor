package com.wimoor.amazon.finances.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmzStorageFeeListDTO对象", description="仓储费查询")
public class AmzStorageFeeListDTO extends BasePageQuery{

	String fromDate;
	String toDate ;
	String marketplaceid;
	String groupid ;
	String search ;
	String searchDate;
}
