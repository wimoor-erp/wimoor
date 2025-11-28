package com.wimoor.amazon.inventory.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryPlanningDTO extends BasePageQuery{

	String groupid;
	String marketplaceid;
	String field;
	String shopid;
	String search;
	String owner;
}
