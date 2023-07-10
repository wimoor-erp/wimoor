package com.wimoor.erp.purchase.alibaba.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaAuth;

public interface IPurchaseAlibabaAuthService extends IService<PurchaseAlibabaAuth> {
//	PurchaseAlibabaAuth save(UserInfo user, String name,String id);
	PurchaseAlibabaAuth bindAuth(UserInfo user, String code, String state);
	List<PurchaseAlibabaAuth> getAuthData(UserInfo user);
	public PurchaseAlibabaAuth refreshAuthToken(PurchaseAlibabaAuth purchaseAlibabaAuth);
	public PurchaseAlibabaAuth refreshAuthRefreshToken(PurchaseAlibabaAuth purchaseAlibabaAuth);
	public JSONObject captureOrderFromAlibaba(UserInfo user, String alibabaAuthid, String alibabaOrderid);
	public JSONObject captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid);
	public JSONObject captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid);
	public String captureRefundByOrderId(UserInfo user, Map<String, String> map);
	public PurchaseAlibabaAuth updateAlibaba(UserInfo user, String id);
	PurchaseAlibabaAuth saveAction(PurchaseAlibabaAuth purchaseAlibabaAuth);
	PurchaseAlibabaAuth selectBymemberId(String memberid);
	public String getAppKey(String id);
	String getJuShiTa();
	Object getAddress(String id);
	JSONObject addFeedback(String alibabaAuthid, String alibabaOrderid, String remark);
	JSONObject cancelOrder(String alibabaAuthid, String alibabaOrderid, String cancelReason, String remark);
	JSONObject productInfo(String alibabaAuthid, String productId);
	JSONObject syncProductListPushed(String alibabaAuthid, String productId);
	boolean checkAuthorityToken(PurchaseAlibabaAuth alibabaAuth);
	JSONObject callApi(String urlPath, String appsecret, Map<String, String> param);
}
