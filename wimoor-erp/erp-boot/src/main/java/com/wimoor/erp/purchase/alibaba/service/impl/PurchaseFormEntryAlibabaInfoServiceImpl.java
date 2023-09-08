package com.wimoor.erp.purchase.alibaba.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryAlibabaInfoMapper;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.ErpPurchaseAlibabaProductitems;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaAuth;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfo;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryLogistics;
import com.wimoor.erp.purchase.alibaba.service.IErpPurchaseAlibabaProductitemsService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@Service("purchaseFormEntryAlibabaInfoService")
@RequiredArgsConstructor
public class PurchaseFormEntryAlibabaInfoServiceImpl  extends ServiceImpl<PurchaseFormEntryAlibabaInfoMapper,PurchaseFormEntryAlibabaInfo>
		implements IPurchaseFormEntryAlibabaInfoService {
	 
	final IPurchaseAlibabaAuthService purchaseAlibabaAuthService;
	 
	final PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;
	
	final IMaterialSupplierService iMaterialSupplierService;
	final IErpPurchaseAlibabaProductitemsService iErpPurchaseAlibabaProductitemsService;
    @Autowired
    @Lazy
    IPurchaseFormEntryService iPurchaseFormEntryService;
	public Boolean unbindAlibabaOrder(UserInfo user, String purchaseEntryid) {
		PurchaseFormEntryAlibabaInfo info = this.getById(purchaseEntryid);
		if(info != null) {
			QueryWrapper<PurchaseFormEntryAlibabaInfo> queryWrapper = new QueryWrapper<PurchaseFormEntryAlibabaInfo>();
			queryWrapper.eq("entryid", purchaseEntryid);
			return this.remove(queryWrapper);
		}
		return false;
	}
	
    public List<String> getEntryIdList(String logisticsId,String shopid){
          return this.baseMapper.getEntryIdByLogisticsId(logisticsId,shopid);
    }
    
	public JSONObject captureAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		PurchaseFormEntryAlibabaInfo oldone = this.getById(purchaseEntryid);
		if(oldone!=null&&oldone.getOrderStatus()!=null&&oldone.getOrderStatus().equals("success")) {
			return GeneralUtil.getJsonObject(oldone.getOrderInfo());
		}
		String status = null;
		JSONObject result =  purchaseAlibabaAuthService.captureOrderFromAlibaba(user,alibabaAuthid,alibabaOrderid);
		if(result != null) {
			JSONObject info=result.getJSONObject("result");
			JSONObject baseInfo=info.getJSONObject("baseInfo");
			if(baseInfo!=null) {
				status = baseInfo.getString("status");
			}
			if (info!=null&&info.containsKey("nativeLogistics")) {
				JSONObject nativeLogistics=info.getJSONObject("nativeLogistics");
				JSONArray logisticsItems = nativeLogistics.getJSONArray("logisticsItems");
				if (logisticsItems != null) {
					for (int j = 0; j < logisticsItems.size(); j++) {
						String logisticsBillNo = logisticsItems.getJSONObject(j).getString("logisticsBillNo");// 物流编号
						QueryWrapper<PurchaseFormEntryLogistics> queryWrapper = new QueryWrapper<PurchaseFormEntryLogistics>();
						queryWrapper.eq("entryid", purchaseEntryid);
						PurchaseFormEntryLogistics logistics=new PurchaseFormEntryLogistics();
						logistics.setEntryid(purchaseEntryid);
						logistics.setLogisticsid(logisticsBillNo);
						logistics.setRefreshtime(new Date());
						if (purchaseFormEntryLogisticsMapper.selectCount(queryWrapper)>0) {
							purchaseFormEntryLogisticsMapper.updateById(logistics);
						
						}else {
							purchaseFormEntryLogisticsMapper.insert(logistics);
						}
					}
				}
			}
			String json = result.toString();
			if (oldone != null) {
				oldone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				oldone.setAlibabaAuth(new BigInteger(alibabaAuthid));
				oldone.setOrderInfo(json);
				oldone.setOrderStatus(status);
				oldone.setOrderRefreshTime(new Date());
				oldone.setLogisticsInfo(null);
				oldone.setLogisticsStatus(null);
				oldone.setLogisticsTraceStatus(null);
				oldone.setLogisticsRefreshTime(null);
				oldone.setLogisticsTraceInfo(null);
				oldone.setLogisticsTraceRefreshTime(null);
				this.baseMapper.updateById(oldone);
			} else {
				PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
				newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newone.setEntryid(purchaseEntryid);
				newone.setOrderStatus(status);
				newone.setOrderInfo(json);
				newone.setOrderStatus(status);
				newone.setOrderRefreshTime(new Date());
				newone.setLogisticsStatus(Boolean.FALSE);
				newone.setLogisticsTraceStatus(Boolean.FALSE);
				this.baseMapper.insert(newone);
			}
            if(info!=null) {
            	JSONArray  productItems=info.getJSONArray("productItems");
				PurchaseFormEntry entry = iPurchaseFormEntryService.getById(purchaseEntryid);
				MaterialSupplier supplier = iMaterialSupplierService.selectSupplier(entry.getMaterialid(),entry.getSupplier());
				for(int i =0 ;i<productItems.size();i++) {
						JSONObject product = productItems.getJSONObject(i);
						String specId=product.getString("specId");
						String productID=product.getString("productID");
						if(supplier!=null&&specId.equals(supplier.getSpecid())&&productID.equals(supplier.getProductcode())) {
							 product.put("isbind","true");
						}else {
							MaterialSupplier supplierother = iMaterialSupplierService.lambdaQuery()
									                         .eq(MaterialSupplier::getProductcode, productID)
									                         .eq(MaterialSupplier::getSupplierid, entry.getSupplier()).one(); 
							if(supplierother==null||supplierother.getMaterialid().equals(entry.getMaterialid())) {
								continue;
							}
							PurchaseFormEntry entryother = iPurchaseFormEntryService.lambdaQuery().eq(PurchaseFormEntry::getFormid, entry.getFormid())
							                                       .eq(PurchaseFormEntry::getMaterialid, supplierother.getMaterialid())
							                                       .eq(PurchaseFormEntry::getAuditstatus, 2).one();
							if(entryother==null||entryother.getId().equals(entry.getId())) {
								continue;
							}
							PurchaseFormEntryAlibabaInfo oldotherone = this.getById(entryother.getId());
							if(oldotherone!=null&&oldotherone.getOrderStatus()!=null&&oldotherone.getOrderStatus().equals("success")) {
									oldotherone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
									oldotherone.setAlibabaAuth(new BigInteger(alibabaAuthid));
									oldotherone.setOrderInfo(json);
									oldotherone.setOrderStatus(status);
									oldotherone.setOrderRefreshTime(new Date());
									oldotherone.setLogisticsInfo(null);
									oldotherone.setLogisticsStatus(null);
									oldotherone.setLogisticsTraceStatus(null);
									oldotherone.setLogisticsRefreshTime(null);
									oldotherone.setLogisticsTraceInfo(null);
									oldotherone.setLogisticsTraceRefreshTime(null);
									this.baseMapper.updateById(oldotherone);
								} else {
									PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
									newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
									newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
									newone.setEntryid(purchaseEntryid);
									newone.setOrderStatus(status);
									newone.setOrderInfo(json);
									newone.setOrderStatus(status);
									newone.setOrderRefreshTime(new Date());
									newone.setLogisticsStatus(Boolean.FALSE);
									newone.setLogisticsTraceStatus(Boolean.FALSE);
									this.baseMapper.insert(newone);
								}
							}
						}
				}
		   }
		return result;
	}
 
	public void refreshAblibabaDateTask(){
		List<PurchaseFormEntryAlibabaInfo> list=this.baseMapper.findNeedRefresh();
		for(PurchaseFormEntryAlibabaInfo item:list) {
			 captureAlibabaOrder(null,item.getAlibabaAuth().toString(),item.getAlibabaOrderid().toString(),item.getEntryid());
			 //captureLogisticsInfo(null,item.getAlibabaAuth().toString(),item.getAlibabaOrderid().toString(),item.getEntryid());
		}
	}
	public JSONObject bindAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		// TODO Auto-generated method stub
		 return captureAlibabaOrder(user,alibabaAuthid,alibabaOrderid,purchaseEntryid);
	}

	public Object captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		// TODO Auto-generated method stub
		PurchaseFormEntryAlibabaInfo oldone = this.baseMapper.selectById(purchaseEntryid);
		Map<String,Object> map = new HashMap<String,Object>();
		if(oldone!=null&&oldone.getOrderStatus()!=null&&oldone.getOrderStatus().equals("success")) {
			map.put("orderResult",GeneralUtil.getJsonObject(oldone.getOrderInfo()));
			map.put("TraceResult", GeneralUtil.getJsonObject(oldone.getLogisticsTraceInfo())  );
			map.put("LogisticsResult",  GeneralUtil.getJsonObject(oldone.getLogisticsInfo()));
			return map;
		}
		map.put("orderResult", this.captureAlibabaOrder(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
		map.put("TraceResult", this.captureLogisticsTraceInfo(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
		JSONObject json = null;
		try {
			json =purchaseAlibabaAuthService.captureLogisticsInfo(user, alibabaAuthid, alibabaOrderid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(json != null && !json.containsKey("errorMessage")) {
			if(oldone == null) {
				PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
				newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newone.setEntryid(purchaseEntryid);
				newone.setLogisticsInfo(json.toString());
				newone.setLogisticsRefreshTime(new Date());
				this.baseMapper.insert(newone);
			}else {
				oldone.setLogisticsInfo(json.toString());
				oldone.setLogisticsRefreshTime(new Date());
				if("success".equals(oldone.getOrderStatus())
						||"cancel".equals(oldone.getOrderStatus())
						||"terminated".equals(oldone.getOrderStatus())
						||"confirm_goods".equals(oldone.getOrderStatus())) {
					oldone.setLogisticsStatus(Boolean.TRUE);
				}
				this.baseMapper.updateById(oldone);
			}
		} 
		map.put("LogisticsResult", json);
		return map;
	}

	public Object captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid,
			String purchaseEntryid) {
		// TODO Auto-generated method stub
		PurchaseFormEntryAlibabaInfo oldone = this.baseMapper.selectById(purchaseEntryid);
		JSONObject json = null;
		json = purchaseAlibabaAuthService.captureLogisticsTraceInfo(user, alibabaAuthid, alibabaOrderid);
        if(json!=null) {
        	oldone.setLogisticsTraceInfo(json.toString());
    		oldone.setLogisticsTraceRefreshTime(new Date());
    		if("success".equals(oldone.getOrderStatus())
    				||"cancel".equals(oldone.getOrderStatus())
    				||"terminated".equals(oldone.getOrderStatus())
    				||"confirm_goods".equals(oldone.getOrderStatus())) {
    			oldone.setLogisticsTraceStatus(Boolean.TRUE);
    		}
    	    this.baseMapper.updateById(oldone);
        }
		return json;
	}
	
	public PurchaseFormEntryAlibabaInfo getEntryAlibabainfo(String purchaseEntryid){
	return this.baseMapper.selectById(purchaseEntryid);
}
	
	public String insetLogisiter(String entryid, String logisiterid) {
		 
		QueryWrapper<PurchaseFormEntryLogistics> queryWrapper = new QueryWrapper<PurchaseFormEntryLogistics>();
		queryWrapper.eq("entryid", entryid);
		List<PurchaseFormEntryLogistics> list = purchaseFormEntryLogisticsMapper.selectList(queryWrapper);
		PurchaseFormEntryLogistics purchaseFormEntryLogistics = null;
		if(list != null && list.size() > 0) {
			purchaseFormEntryLogistics = list.get(0);
			String logisiter = purchaseFormEntryLogistics.getLogisticsid();
			if((GeneralUtil.isNotEmpty(logisiterid) )) {
				if(!logisiterid.equals(logisiter)) {
					purchaseFormEntryLogistics.setLogisticsid(logisiterid);
					purchaseFormEntryLogistics.setRefreshtime(new Date());
					purchaseFormEntryLogisticsMapper.updateById(purchaseFormEntryLogistics);
				}
			}else {
				purchaseFormEntryLogisticsMapper.deleteById(purchaseFormEntryLogistics.getEntryid());
			}
		}else {
			if(GeneralUtil.isNotEmpty(logisiterid)) {
				purchaseFormEntryLogistics = new PurchaseFormEntryLogistics();
				purchaseFormEntryLogistics.setEntryid(entryid);
				purchaseFormEntryLogistics.setLogisticsid(logisiterid);
				purchaseFormEntryLogistics.setRefreshtime(new Date());
				purchaseFormEntryLogisticsMapper.insert(purchaseFormEntryLogistics);
			}
		}
		return "SUCESS";
	}
	public String deleteLogisiter(String entryid, String logisiterid) {
		// TODO Auto-generated method stub
		QueryWrapper<PurchaseFormEntryLogistics> queryWrapper = new QueryWrapper<PurchaseFormEntryLogistics>();
		queryWrapper.eq("entryid", entryid);
		List<PurchaseFormEntryLogistics> list = purchaseFormEntryLogisticsMapper.selectList(queryWrapper);
		if(list != null && list.size() > 0) {
			purchaseFormEntryLogisticsMapper.deleteById(list.get(0).getEntryid());
		}else {
			throw new ERPBizException("该订单没有关联其他运单号！");
		}
		return "SUCESS";
	}

	@Override
	public PurchaseFormEntryAlibabaInfo selectByOrderAndAuth(String authid, String orderid) {
		QueryWrapper<PurchaseFormEntryAlibabaInfo> queryWrapper=new QueryWrapper<PurchaseFormEntryAlibabaInfo>();
		queryWrapper.eq("alibabaAuth", authid);
		queryWrapper.eq("alibabaOrderid", orderid);
		List<PurchaseFormEntryAlibabaInfo> list = this.baseMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}


	@Override
	public JSONObject preview(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String alibabaAuthid=params.get("alibabaAuthid").toString();
		String address= params.get("address").toString();
		String product= params.get("product").toString();
		PurchaseAlibabaAuth alibabaAuth = purchaseAlibabaAuthService.getById(alibabaAuthid);
		purchaseAlibabaAuthService.checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("addressParam",address.toString());
		param.put("cargoParamList",product.toString());
		param.put("flow","general");
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.createOrder.preview", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}

	@Override
	public JSONObject createCrossOrder(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String alibabaAuthid=params.get("alibabaAuthid").toString();
		String address= params.get("address").toString();
		String product= params.get("product").toString();
		PurchaseAlibabaAuth alibabaAuth = purchaseAlibabaAuthService.getById(alibabaAuthid);
		purchaseAlibabaAuthService.checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("addressParam",address.toString());
		param.put("cargoParamList",product.toString());
		param.put("flow","general");
		if(params.get("message")!=null) {
			param.put("message",params.get("message").toString());
		}
		if(params.get("promotion")!=null) {
			param.put("shopPromotionId",params.get("promotion").toString());
		}
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.createCrossOrder", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}
	
	@Override
	public JSONObject getRefundReasonList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String alibabaAuthid=params.get("alibabaAuthid").toString();
		String alibabaOrderid= params.get("alibabaOrderid").toString();
		String orderEntryIds= params.get("orderEntryIds").toString();
		orderEntryIds=orderEntryIds.replace("\"", "");
		String goodsStatus=params.get("goodsStatus").toString();
		PurchaseAlibabaAuth alibabaAuth = purchaseAlibabaAuthService.getById(alibabaAuthid);
		purchaseAlibabaAuthService.checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("orderId",alibabaOrderid);
		param.put("orderEntryIds",orderEntryIds);
		param.put("goodsStatus",goodsStatus);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.getRefundReasonList", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}
	
	@Override
	public JSONObject createRefund(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String alibabaAuthid=params.get("alibabaAuthid").toString();
		String alibabaOrderid= params.get("alibabaOrderid").toString();
		String orderEntryIds= params.get("orderEntryIds").toString();
		String goodsStatus=params.get("goodsStatus").toString();
		String applyPayment=params.get("applyPayment").toString();
		String applyCarriage=params.get("applyCarriage").toString();
		String applyReasonId=params.get("applyReasonId").toString();
		String description=params.get("description").toString();
		String disputeRequest=params.get("disputeRequest").toString();
		orderEntryIds=orderEntryIds.replace("\"", "");
		PurchaseAlibabaAuth alibabaAuth = purchaseAlibabaAuthService.getById(alibabaAuthid);
		purchaseAlibabaAuthService.checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("orderId",alibabaOrderid);
		param.put("orderEntryIds",orderEntryIds);
		param.put("goodsStatus",goodsStatus);
		param.put("disputeRequest",disputeRequest);
		Integer pay=new BigDecimal(applyPayment).multiply(new BigDecimal("100")).intValue();
		param.put("applyPayment",pay.toString());
		Integer carriage=new BigDecimal(applyCarriage).multiply(new BigDecimal("100")).intValue();
		param.put("applyCarriage",carriage.toString());
		param.put("applyReasonId",applyReasonId);
		param.put("description",description);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.createRefund", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}
	
	@Override
	public JSONObject payWay(String alibabaAuthid,String orderid) {
		// TODO Auto-generated method stub
		PurchaseAlibabaAuth alibabaAuth = purchaseAlibabaAuthService.getById(alibabaAuthid);
		purchaseAlibabaAuthService.checkAuthorityToken(alibabaAuth);
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("orderId",orderid);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.trade.payWay.query", 1, "param2", alibabaAuth.getAppkey());
		JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
		return json;
	}

	@Override
	public JSONObject payUrl(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String alibabaAuthid=params.get("alibabaAuthid").toString();
		String orderIdList=params.get("orderIdList").toString();
		String channel=params.get("channel").toString();
		PurchaseAlibabaAuth alibabaAuth = purchaseAlibabaAuthService.getById(alibabaAuthid);
		purchaseAlibabaAuthService.checkAuthorityToken(alibabaAuth);
		orderIdList=orderIdList.replace("\"", "");
		Map<String, String> param = new HashMap<String, String>();
		param.put("access_token", alibabaAuth.getAccessToken());
		param.put("orderIdList",orderIdList);
		if(channel.equals("1")) {
			String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.alipay.url.get", 1, "param2", alibabaAuth.getAppkey());
			JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
			return json;
		}else if(channel.equals("3")) {
			String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.creditPay.url.get", 1, "param2", alibabaAuth.getAppkey());
			JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
			return json;
		}else if(channel.equals("16")||channel.equals("20")){
			String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.crossBorderPay.url.get", 1, "param2", alibabaAuth.getAppkey());
			JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
			return json;
		}else {
			String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade","alibaba.alipay.url.get", 1, "param2", alibabaAuth.getAppkey());
			JSONObject json = purchaseAlibabaAuthService.callApi(urlPath,alibabaAuth.getAppsecret(),param);
			return json;
		}
	
	}

	 
}
