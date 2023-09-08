package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;

public interface  IPurchasePlanService extends IService<PurchasePlan> {

	public Map<String,Object> getLast3Form(Map<String, Object> map);
	public String getLastForm(Map<String, Object> map);
	public Map<String,Object> getLastForms(List<String> ids);
	public void changePurchasePlanModelItem(Map<String, Object> param2);

	public List<PurchasePlan> getPlanByShopid(String shopid) throws ERPBizException;
	public PurchasePlan savePlan(PurchasePlan plan);

	public PurchasePlan updatePlan(PurchasePlan plan);
	public void afterSavePOSubPlan(UserInfo user, String id);
}
