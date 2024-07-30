package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanConsumableItem;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-05-08
 */
public interface IPurchasePlanConsumableItemService extends IService<PurchasePlanConsumableItem> {

	Map<String,Object> getSummary(String shopid, String planid);
	public List<Map<String, Object>> getList(String shopid, String warehouseid);
}
