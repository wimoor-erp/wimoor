package com.wimoor.erp.change.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeReceive;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

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
