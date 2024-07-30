package com.wimoor.erp.change.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.change.pojo.dto.PurchaseFormEntryChangeVo;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChange;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
public interface IPurchaseFormEntryChangeService extends IService<PurchaseFormEntryChange> {

	IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map);


	Map<String, Object> saveAction(PurchaseFormEntryChangeVo inWarehouseForm, String sku, UserInfo user);


	void examineChange(PurchaseFormEntryChange entry, int banlance,UserInfo user);


	void resetForm(PurchaseFormEntryChange entry);


}
