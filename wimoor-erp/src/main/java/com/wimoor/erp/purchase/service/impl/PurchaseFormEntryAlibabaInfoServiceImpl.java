package com.wimoor.erp.purchase.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.trade.param.AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo;
import com.alibaba.trade.param.AlibabaOpenplatformTradeModelTradeInfo;
import com.alibaba.trade.param.AlibabaTradeGetBuyerViewResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryAlibabaInfoMapper;
import com.wimoor.erp.purchase.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryAlibabaInfo;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntryLogistics;
import com.wimoor.erp.purchase.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryAlibabaInfoService;

import lombok.RequiredArgsConstructor;

@Service("purchaseFormEntryAlibabaInfoService")
@RequiredArgsConstructor
public class PurchaseFormEntryAlibabaInfoServiceImpl  extends ServiceImpl<PurchaseFormEntryAlibabaInfoMapper,PurchaseFormEntryAlibabaInfo>
		implements IPurchaseFormEntryAlibabaInfoService {
	 
	IPurchaseAlibabaAuthService purchaseAlibabaAuthService;
	 
	PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;

	public Object unbindAlibabaOrder(UserInfo user, String purchaseEntryid) {
		PurchaseFormEntryAlibabaInfo info = this.getById(purchaseEntryid);
		if(info != null) {
			QueryWrapper<PurchaseFormEntryAlibabaInfo> queryWrapper = new QueryWrapper<PurchaseFormEntryAlibabaInfo>();
			queryWrapper.eq("entryid", purchaseEntryid);
			return this.remove(queryWrapper);
		}
		return null;
	}
	
    public List<String> getEntryIdList(String logisticsId,String shopid){
          return this.baseMapper.getEntryIdByLogisticsId(logisticsId,shopid);
    }
	public String captureAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		PurchaseFormEntryAlibabaInfo oldone = this.getById(purchaseEntryid);
		String status = null;
		AlibabaTradeGetBuyerViewResult result = null;
		if (oldone != null && oldone.getOrderRefreshTime() != null && oldone.getOrderInfo() != null) {
			if ("success".equals(oldone.getOrderStatus()) || "cancel".equals(oldone.getOrderStatus())
					|| "terminated".equals(oldone.getOrderStatus())) {
				return oldone.getOrderInfo();
			} else if (GeneralUtil.distanceOfMinutes(oldone.getOrderRefreshTime(), new Date())<30) {
				return oldone.getOrderInfo();
			}
		}
		try {
			result = purchaseAlibabaAuthService.captureOrderFromAlibaba(user,alibabaAuthid,alibabaOrderid);
		}
		catch(ERPBizException e){
			e.printStackTrace();
			if (GeneralUtil.isNotEmpty(e.getMessage())) {
				String message = e.getMessage();
				if (message.contains("PlumMsgStart:") && message.contains(":PlumMsgEnd")) {
					String messages[] = message.split(":");
					message = messages[1];
					throw new ERPBizException(message);
				}
			}
		}
		if(result != null) {
			AlibabaOpenplatformTradeModelTradeInfo tradeModelTradeInfo = result.getResult();
			if (tradeModelTradeInfo != null&& tradeModelTradeInfo.getNativeLogistics()!=null) {
				AlibabaOpenplatformTradeModelNativeLogisticsItemsInfo[] logisticsItems = tradeModelTradeInfo.getNativeLogistics().getLogisticsItems();
				status = tradeModelTradeInfo.getBaseInfo().getStatus();
				if (logisticsItems != null) {
					for (int j = 0; j < logisticsItems.length; j++) {
						String logisticsBillNo = logisticsItems[j].getLogisticsBillNo();// 物流编号
						QueryWrapper<PurchaseFormEntryLogistics> queryWrapper = new QueryWrapper<PurchaseFormEntryLogistics>();
						queryWrapper.eq("entryid", purchaseEntryid);
						PurchaseFormEntryLogistics logistics=new PurchaseFormEntryLogistics();
						logistics.setEntryid(purchaseEntryid);
						logistics.setLogisticsid(logisticsBillNo);
						if (purchaseFormEntryLogisticsMapper.selectCount(queryWrapper)>0) {
							purchaseFormEntryLogisticsMapper.updateById(logistics);
						
						}else {
							purchaseFormEntryLogisticsMapper.insert(logistics);
						}
					}
				}
			}
			String json = JSONObject.toJSONString(result);
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
				newone.setOrderStatus(tradeModelTradeInfo.getBaseInfo().getStatus());
				newone.setOrderRefreshTime(new Date());
				newone.setLogisticsStatus(Boolean.FALSE);
				newone.setLogisticsTraceStatus(Boolean.FALSE);
				this.baseMapper.insert(newone);
			}
			return json;
		}
		return null;
	}
	
	public void refreshAblibabaDateTask(){
		List<PurchaseFormEntryAlibabaInfo> list=this.baseMapper.findNeedRefresh();
		for(PurchaseFormEntryAlibabaInfo item:list) {
			 captureAlibabaOrder(null,item.getAlibabaAuth().toString(),item.getAlibabaOrderid().toString(),item.getEntryid());
			 //captureLogisticsInfo(null,item.getAlibabaAuth().toString(),item.getAlibabaOrderid().toString(),item.getEntryid());
		}
	}
	public Object bindAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		// TODO Auto-generated method stub
		String json  = captureAlibabaOrder(user,alibabaAuthid,alibabaOrderid,purchaseEntryid);
		return json;
	}

	public Object captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		// TODO Auto-generated method stub
		PurchaseFormEntryAlibabaInfo oldone = this.baseMapper.selectById(purchaseEntryid);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderResult", this.captureAlibabaOrder(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
		map.put("TraceResult", this.captureLogisticsTraceInfo(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
		String json = null;
		if(oldone != null && oldone.getLogisticsRefreshTime() != null && oldone.getLogisticsInfo() != null) {
			 if(oldone.getLogisticsStatus()){
				map.put("LogisticsResult", oldone.getLogisticsInfo());
				return map;
			}else if(GeneralUtil.distanceOfMinutes(oldone.getLogisticsRefreshTime(), new Date())<10) {
				map.put("LogisticsResult", oldone.getLogisticsInfo());
				return map;
			}
		}
		try {
			json =purchaseAlibabaAuthService.captureLogisticsInfo(user, alibabaAuthid, alibabaOrderid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(json != null && !json.contains("errorMessage")) {
			if(oldone == null) {
				PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
				newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newone.setEntryid(purchaseEntryid);
				newone.setLogisticsInfo(json);
				newone.setLogisticsRefreshTime(new Date());
				this.baseMapper.insert(newone);
			}else {
				oldone.setLogisticsInfo(json);
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
		String json = null;
		if(oldone != null && oldone.getLogisticsTraceRefreshTime() != null && oldone.getLogisticsTraceInfo() != null) {
			if(oldone.getLogisticsTraceStatus()) {
				return oldone.getLogisticsTraceInfo();
			}else if(GeneralUtil.distanceOfMinutes(oldone.getLogisticsTraceRefreshTime(), new Date())<10) {
				return oldone.getLogisticsTraceInfo();
			}
		}
		json = purchaseAlibabaAuthService.captureLogisticsTraceInfo(user, alibabaAuthid, alibabaOrderid);
        oldone.setLogisticsTraceInfo(json);
		oldone.setLogisticsTraceRefreshTime(new Date());
		if("success".equals(oldone.getOrderStatus())
				||"cancel".equals(oldone.getOrderStatus())
				||"terminated".equals(oldone.getOrderStatus())
				||"confirm_goods".equals(oldone.getOrderStatus())) {
			oldone.setLogisticsTraceStatus(Boolean.TRUE);
		}
	    this.baseMapper.updateById(oldone);
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

}
