package com.wimoor.amazon.inventory.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryCostDTO extends BasePageQuery{
	String marketplaceid ;
	String sku;
	String groupid ;
	String byday ;
}
