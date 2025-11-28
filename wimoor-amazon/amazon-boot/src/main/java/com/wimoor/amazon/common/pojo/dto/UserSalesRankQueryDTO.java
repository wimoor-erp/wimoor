package com.wimoor.amazon.common.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSalesRankQueryDTO  extends BasePageQuery{
	String daytype;
}
