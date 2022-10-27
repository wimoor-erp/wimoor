package com.wimoor.amazon.inventory.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventorySizeDTO extends BasePageQuery{
	String groupid;
	String marketplaceid;
	String search;
	String country;
	String sizetype;
	String isgtself;
}
