package com.wimoor.erp.inventory.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.inventory.mapper.StockTakingItemMapper;
import com.wimoor.erp.inventory.pojo.entity.StockTakingItem;
import com.wimoor.erp.inventory.service.IStockTakingItemService;

@Service("stockTakingItemService")
public class StockTakingItemServiceImpl extends  ServiceImpl<StockTakingItemMapper,StockTakingItem> implements IStockTakingItemService {
	  

	public List<StockTakingItem> findByContion(String stocktakingid, String materialid, String warehouseid) {
		QueryWrapper<StockTakingItem> queryWrapper = new QueryWrapper<StockTakingItem>();
		queryWrapper.eq("stocktakingid", stocktakingid);
		queryWrapper.eq("materialid", materialid);
		queryWrapper.eq("warehouseid", warehouseid);
		List<StockTakingItem> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}
	
}
