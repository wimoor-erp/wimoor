package com.wimoor.amazon.report.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SalesDTO对象", description="查询库存")
public class SalesDTO extends BasePageQuery{
	String groupid;
	String region;
	String owner;
	String color;
	String search;
	String marketplace;
	String fromdatestr;
	String enddatestr;
	String summaryType;
}
