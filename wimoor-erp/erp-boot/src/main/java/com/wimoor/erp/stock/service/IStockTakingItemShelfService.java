package com.wimoor.erp.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.wimoor.erp.stock.pojo.entity.StockTakingItemShelf;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-03
 */
public interface IStockTakingItemShelfService extends IService<StockTakingItemShelf> {

	void stockTakingInvOperate(StockTaking stocktaking, UserInfo user);

}
