package com.wimoor.erp.stock.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;

public interface IStockTakingItemService extends IService<StockTakingItem> {

	List<StockTakingItem> findByContion(String stocktakingid, String materialid, String warehouseid);

}
