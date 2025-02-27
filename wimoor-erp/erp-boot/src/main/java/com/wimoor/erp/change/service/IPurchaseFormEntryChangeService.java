package com.wimoor.erp.change.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.change.pojo.dto.PurchaseFormEntryChangeDTO;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChange;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeAttachment;

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


	Map<String, Object> saveAction(PurchaseFormEntryChangeDTO inWarehouseForm, UserInfo user, InputStream input, String fileName);


	void examineChange(PurchaseFormEntryChange entry, int banlance,UserInfo user);


	void resetForm(PurchaseFormEntryChange entry);

	public void saveFormAttachments(UserInfo user,PurchaseFormEntryChange entry, InputStream in,String imageName) throws IOException;

	public void deleteFormAttachments(String id) throws IOException;

	List<PurchaseFormEntryChangeAttachment> getAttachments(String id);

    void handleAssemblyMaterial(PurchaseFormEntryChange entry, UserInfo user);

	void handleInventory(PurchaseFormEntryChange entry, UserInfo user);
}
