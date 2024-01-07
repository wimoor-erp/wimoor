package com.wimoor.erp.purchase.alibaba.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfo;

public interface IPurchaseFormEntryAlibabaInfoService extends IService<PurchaseFormEntryAlibabaInfo> {
	
	JSONObject bindAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);

	Object captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);
	
	Object captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);
	
	public JSONObject captureAlibabaOrderInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid);
	
	public Boolean unbindAlibabaOrder(UserInfo user,String purchaseEntryid);
	
	public List<String> getEntryIdList(String logisticsId,String shopid);
	
	void refreshAblibabaDateTask();
	
	public PurchaseFormEntryAlibabaInfo getEntryAlibabainfo(String entry_id);
	
	public String insetLogisiter(String entryid, String logisiterid);
	
	public String deleteLogisiter(String entryid, String logisiterid);

	PurchaseFormEntryAlibabaInfo selectByOrderAndAuth(String authid, String orderid);

	JSONObject preview(Map<String, Object> param);

	JSONObject createCrossOrder(Map<String, Object> params);

	JSONObject payWay(String alibabaAuthid, String orderid);

	JSONObject payUrl(Map<String, Object> param);

	JSONObject getRefundReasonList(Map<String, Object> params);

	JSONObject createRefund(Map<String, Object> params);

	public void checkPay(BigInteger number,BigDecimal price, String acct);

}
