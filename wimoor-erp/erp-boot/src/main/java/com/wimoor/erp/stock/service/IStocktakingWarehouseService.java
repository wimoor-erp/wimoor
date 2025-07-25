package com.wimoor.erp.stock.service;

import com.wimoor.erp.stock.pojo.entity.StocktakingWarehouse;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-05
 */
public interface IStocktakingWarehouseService extends IService<StocktakingWarehouse> {

	void saveData(List<StocktakingWarehouse> warehouselist);
  	public List<StocktakingWarehouse> listData(String stocktakingid);
	void cancel(String id);
	void end(String id);
}
