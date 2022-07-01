package com.wimoor.erp.purchase.service;

import java.math.BigInteger;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryAlibabaInfo;

public interface IPurchaseFormEntryAlibabaInfoService extends IService<PurchaseFormEntryAlibabaInfo> {
	
	Object bindAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);

	Object captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);
	
	Object captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);
	
	public Object unbindAlibabaOrder(UserInfo user,String purchaseEntryid);
	
	public List<String> getEntryIdList(String logisticsId,String shopid);
	
	void refreshAblibabaDateTask();
	
	public PurchaseFormEntryAlibabaInfo getEntryAlibabainfo(String entry_id);
	
	public String insetLogisiter(String entryid, String logisiterid);
	
	public String deleteLogisiter(String entryid, String logisiterid);

	PurchaseFormEntryAlibabaInfo selectByOrderAndAuth(String authid, String orderid);
}
