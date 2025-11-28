package com.wimoor.erp.stock.service;

import com.wimoor.erp.stock.pojo.entity.StocktakingShelf;

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
public interface IStocktakingShelfService extends IService<StocktakingShelf> {

	void saveData(List<StocktakingShelf> shelflist);
 	public List<StocktakingShelf> listData(String stocktakingid);
	void cancel(String id);
	void end(String id);
}
