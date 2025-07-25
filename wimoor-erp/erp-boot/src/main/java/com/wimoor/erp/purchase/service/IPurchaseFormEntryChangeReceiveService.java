package com.wimoor.erp.purchase.service;

import com.wimoor.common.user.UserInfo;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryChangeReceive;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
public interface IPurchaseFormEntryChangeReceiveService extends IService<PurchaseFormEntryChangeReceive> {

	Map<String, Object> saveMineAndinStock(PurchaseFormEntryChangeReceive assembRecord, UserInfo user);

	void cancelInstock(PurchaseFormEntryChangeReceive receive, UserInfo user);

}
