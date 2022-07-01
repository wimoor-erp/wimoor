package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import com.alibaba.trade.param.AlibabaTradeGetBuyerViewResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.pojo.entity.PurchaseAlibabaAuth;

public interface IPurchaseAlibabaAuthService extends IService<PurchaseAlibabaAuth> {
//	PurchaseAlibabaAuth save(UserInfo user, String name,String id);
	PurchaseAlibabaAuth bindAuth(UserInfo user, String code, String state);
	List<PurchaseAlibabaAuth> getAuthData(UserInfo user);
	public PurchaseAlibabaAuth refreshAuthToken(PurchaseAlibabaAuth purchaseAlibabaAuth);
	public PurchaseAlibabaAuth refreshAuthRefreshToken(PurchaseAlibabaAuth purchaseAlibabaAuth);
	public AlibabaTradeGetBuyerViewResult captureOrderFromAlibaba(UserInfo user, String alibabaAuthid, String alibabaOrderid);
	public String captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid);
	public String captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid);
	public String captureRefundByOrderId(UserInfo user, Map<String, String> map);
	public PurchaseAlibabaAuth updateAlibaba(UserInfo user, String id);
	PurchaseAlibabaAuth saveAction(PurchaseAlibabaAuth purchaseAlibabaAuth);
	PurchaseAlibabaAuth selectBymemberId(String memberid);
	public String getAppKey(String id);
}
