package com.wimoor.amazon.profit.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ProfitSettingDTO extends BasePageQuery {
	String country;
	String search;
	String tierid;
	String dispatchtype;
	Boolean isclothing;
}
