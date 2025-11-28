package com.wimoor.admin.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryShopNameDTO extends BasePageQuery{

	String name;
	
	String roleid;
	
	String pkgid;
	
}
