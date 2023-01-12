package com.wimoor.amazon.inventory.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.model.fbainventory.InventorySummary;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;

public interface IInventorySupplyService {

	Map<String, InventorySummary> captureInventorySupplyNew(AmazonAuthority amazonAuthority, List<String> list);
	Map<String, InventorySummary> captureInventorySupplyNew(AmazonAuthority amazonAuthority, Date date);
	public Map<String,InventorySummary> captureInventorySupply(AmazonAuthority amazonAuthority,List<String> skulist);
    List<ProductInventoryVo> findFBA(String groupid,
			 String warehouseid,
			 String sku,
			 String myself,
			 String shopid);
	InventoryReport syncInventorySupply(AmazonAuthority amazonAuthority, List<String> list);
	List<AmzInventoryCountryReport> findEUFBA(String authid, String sku);
}
