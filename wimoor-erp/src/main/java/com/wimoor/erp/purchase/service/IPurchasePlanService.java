package com.wimoor.erp.purchase.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanItem;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlanModel;

public interface  IPurchasePlanService extends IService<PurchasePlan> {
	
	public List<Map<String, Object>> calPlanDetail(Map<String, Object> param)throws ERPBizException ;
	
	public int savePlanItem(PurchasePlanItem item, String oldplanitemid, String oldplanitemamount) throws ERPBizException;
	
	public int disablePlanItem(UserInfo user,String planid, String materialid, String warehouseid) throws ERPBizException;
	
	public void refreshModel(UserInfo user, String planid,String warehouseid, String modelid) throws ERPBizException;
	
    public PurchasePlanModel getRefreshModelTime(String planid);
    
	public List<Map<String, Object>> getSummaryPlan(String planid);
	
	public List<Map<String, Object>> getSummaryPlanOrder(String planid, String warehouseid, List<String> item_material_list);
	
	public List<Map<String, Object>> getSummaryPlanByWarehouse(String planid, String warehouseid, List<String> item_material_list);
	
	public void afterSavePOForm(String planitem, String warehouseid) throws ERPBizException;
	
	public List<Map<String, Object>> getSummaryPlanAssembly(String planid, String warehouseid, List<String> item_material_list);
	
	public void afterSaveASForm(String planitemid, String warehouseid) throws ERPBizException;
	
	public void hideMaterilItem(String materialid, HttpServletRequest request) throws ERPBizException;
	
	public void showMaterilItem(String materialid);
	
	public Map<String,Object> getLast3Form(Map<String, Object> map);
	
	public PurchasePlan cancelPO(UserInfo user, String poid) throws ERPBizException;
	
	public PurchasePlan cancelAO(UserInfo user, String aoid) throws ERPBizException;
	
	public Map<String,Object> ChangeMainGroup(String groupid, String warehouseid) throws ERPBizException;
	
	public IPage<Map<String, Object>> getPlan(Page<?> page,Map<String, Object> map);
	
	public void deletePlanNew(UserInfo user, String planid);
	
	public PurchasePlan readWorkStatusByPlanId(UserInfo user, String planid) throws ERPBizException ;
	
	public PurchasePlan getPlanByShopid(String shopid,String warehouseid) throws ERPBizException;
	
	public Map<String, Object> changePlanDetailByPlan(UserInfo user,PurchasePlan oldplan) throws ERPBizException ;
	

	
	
	public List<Map<String, Object>> getPurchasePlanForShopId(String shopid);
	
	public Map<String, Object> getNotPlanWarehouse(String shopid);
	
	public Map<String, Object> refreshPlanModel(UserInfo user, String planid, String warehouseid) throws ERPBizException;

	public void changePurchasePlanModelItem(Map<String, Object> param2);
	
	public void afterSaveASSubplan(UserInfo user,String warehouseid) throws ERPBizException;
	public void afterSavePOSubPlan(UserInfo user,String warehouseid) throws ERPBizException;
}
