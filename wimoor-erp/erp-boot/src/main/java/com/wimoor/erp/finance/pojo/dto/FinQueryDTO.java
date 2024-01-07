package com.wimoor.erp.finance.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="FinQueryDTO对象", description="查询财务记账")
public class FinQueryDTO extends BasePageQuery{
	String search;
	String project;
	String fromDate;
	String toDate;
	String createdate ;
	String sumType;
	String acc;
}
