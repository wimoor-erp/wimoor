package com.wimoor.erp.stock.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;

public interface IStockTakingItemService extends IService<StockTakingItem> {

	List<StockTakingItem> findByContion(String stocktakingid, String materialid, String warehouseid);

	void stockTakingInvOperate(StockTaking stocktaking, UserInfo user);

	List<Map<String, Object>> getItemList(String id, String warehouseid, String search);

	IPage<Map<String, Object>> findLocalInventory(Page<Object> page, Map<String, Object> param);

}
