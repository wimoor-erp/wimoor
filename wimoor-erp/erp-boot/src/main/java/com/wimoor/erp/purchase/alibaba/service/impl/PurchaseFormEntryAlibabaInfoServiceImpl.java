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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryAlibabaInfoExtMapper;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryAlibabaInfoMapper;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaAuth;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfo;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfoExt;
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
	final PurchaseFormEntryAlibabaInfoExtMapper purchaseFormEntryAlibabaInfoExtMapper;
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
    public JSONObject captureAlibabaOrderInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		PurchaseFormEntryAlibabaInfo oldone = this.getById(purchaseEntryid);
		  PurchaseFormEntryAlibabaInfoExt oldext = purchaseFormEntryAlibabaInfoExtMapper.selectById(purchaseEntryid);
		String status = null;
		JSONObject result =  purchaseAlibabaAuthService.captureOrderFromAlibaba(user,alibabaAuthid,alibabaOrderid);
		if(result != null) {
			JSONObject info=result.getJSONObject("result");
			if(info==null&&result.getString("success").equals("false")) {
				String msg = result.getString("errorMessage");
				if(msg!=null) {
					JSONObject json = GeneralUtil.getJsonObject(msg);
					throw new BizException(json.getString("errorMessage"));
				}else {
					throw new BizException("绑定失败，无法找到对应订单信息");
				}
				
			}
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
				oldext.setOrderStatus(status);
				oldext.setOrderRefreshTime(new Date());
				this.baseMapper.updateById(oldone);
				this.purchaseFormEntryAlibabaInfoExtMapper.updateById(oldext);
				
			} else {
				PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
				PurchaseFormEntryAlibabaInfoExt newext = new PurchaseFormEntryAlibabaInfoExt();
				newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newone.setEntryid(purchaseEntryid);
				
				newext.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newext.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newext.setEntryid(purchaseEntryid);
				
				newext.setOrderStatus(status);
				newone.setOrderInfo(json);
				newext.setOrderStatus(status);
				newext.setOrderRefreshTime(new Date());
				newext.setLogisticsStatus(Boolean.FALSE);
				newext.setLogisticsTraceStatus(Boolean.FALSE);
				this.baseMapper.insert(newone);
				this.purchaseFormEntryAlibabaInfoExtMapper.insert(newext);
			}
           
		   }
		return result;
	}
	public JSONObject captureAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		PurchaseFormEntryAlibabaInfo oldone = this.getById(purchaseEntryid);
		  PurchaseFormEntryAlibabaInfoExt oldext = purchaseFormEntryAlibabaInfoExtMapper.selectById(purchaseEntryid);
		if(oldone!=null&&oldext!=null&&oldext.getOrderStatus()!=null&&oldext.getOrderStatus().equals("success")) {
			return GeneralUtil.getJsonObject(oldone.getOrderInfo());
		}
		String status = null;
		JSONObject result =  purchaseAlibabaAuthService.captureOrderFromAlibaba(user,alibabaAuthid,alibabaOrderid);
		if(result != null) {
			JSONObject info=result.getJSONObject("result");
			if(info==null&&result.getString("success").equals("false")) {
				String msg = result.getString("errorMessage");
				if(msg!=null) {
					if(msg instanceof String) {
						throw new BizException(msg);
					}else {
						JSONObject json = GeneralUtil.getJsonObject(msg);
						throw new BizException(json.getString("errorMessage"));
					}
					
				}else {
					throw new BizException("绑定失败，无法找到对应订单信息");
				}
				
			}
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
				oldext.setOrderStatus(status);
				oldext.setOrderRefreshTime(new Date());
				this.baseMapper.updateById(oldone);
				this.purchaseFormEntryAlibabaInfoExtMapper.updateById(oldext);
				
			} else {
				PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
				PurchaseFormEntryAlibabaInfoExt newext = new PurchaseFormEntryAlibabaInfoExt();
				newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newone.setEntryid(purchaseEntryid);
				
				newext.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newext.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newext.setEntryid(purchaseEntryid);
				
				newext.setOrderStatus(status);
				newone.setOrderInfo(json);
				newext.setOrderStatus(status);
				newext.setOrderRefreshTime(new Date());
				newext.setLogisticsStatus(Boolean.FALSE);
				newext.setLogisticsTraceStatus(Boolean.FALSE);
				this.baseMapper.insert(newone);
				this.purchaseFormEntryAlibabaInfoExtMapper.insert(newext);
			}
            if(info!=null&&user!=null&&oldext==null) {
            	JSONArray  productItems=info.getJSONArray("productItems");
				PurchaseFormEntry entry = iPurchaseFormEntryService.getById(purchaseEntryid);
				MaterialSupplier supplier = iMaterialSupplierService.selectSupplier(entry.getMaterialid(),entry.getSupplier());
				for(int i =0 ;i<productItems.size();i++) {
						JSONObject product = productItems.getJSONObject(i);
						String specId=product.getString("specId");
						String productID=product.getString("productID");
						if(supplier!=null&&supplier.getSpecid()!=null&&supplier.getProductcode()!=null&&specId.equals(supplier.getSpecid())&&productID.equals(supplier.getProductcode())) {
							 product.put("isbind","true");
						}else {
							List<MaterialSupplier> supplierothers = iMaterialSupplierService.lambdaQuery()
									                         .eq(MaterialSupplier::getProductcode, productID)
									                         .eq(MaterialSupplier::getSupplierid, entry.getSupplier()).list(); 
							for(MaterialSupplier supplierother:supplierothers) {
								if(supplierother==null||supplierother.getMaterialid().equals(entry.getMaterialid())) {
									continue;
								}
								PurchaseFormEntry entryother = iPurchaseFormEntryService.lambdaQuery().eq(PurchaseFormEntry::getFormid, entry.getFormid())
								                                       .eq(PurchaseFormEntry::getMaterialid, supplierother.getMaterialid())
								                                       .eq(PurchaseFormEntry::getAuditstatus, 2).one();
								if(entryother==null||entryother.getId().equals(entry.getId())) {
									continue;
								}
								PurchaseFormEntryAlibabaInfoExt oldotherext = this.purchaseFormEntryAlibabaInfoExtMapper.selectById(entryother.getId());
								if(oldotherext!=null&&oldotherext.getOrderStatus()!=null&&oldotherext.getOrderStatus().equals("success")) {
									    PurchaseFormEntryAlibabaInfo oldotherone = this.baseMapper.selectById(entryother.getId());
										oldotherone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
										oldotherone.setAlibabaAuth(new BigInteger(alibabaAuthid));
										oldotherone.setOrderInfo(json);
										oldotherext.setOrderStatus(status);
										oldotherext.setOrderRefreshTime(new Date());
										this.baseMapper.updateById(oldotherone);
										this.purchaseFormEntryAlibabaInfoExtMapper.updateById(oldotherext);
									} else {
										PurchaseFormEntryAlibabaInfo old = this.baseMapper.selectById(entryother.getId());
										PurchaseFormEntryAlibabaInfoExt oext = this.purchaseFormEntryAlibabaInfoExtMapper.selectById(entryother.getId());
										if(old!=null) {
											old.setAlibabaOrderid(new BigInteger(alibabaOrderid));
											old.setAlibabaAuth(new BigInteger(alibabaAuthid));
											oext.setAlibabaOrderid(new BigInteger(alibabaOrderid));
											oext.setAlibabaAuth(new BigInteger(alibabaAuthid));
											oext.setOrderStatus(status);
											old.setOrderInfo(json);
											oext.setOrderStatus(status);
											oext.setOrderRefreshTime(new Date());
											oext.setLogisticsStatus(Boolean.FALSE);
											oext.setLogisticsTraceStatus(Boolean.FALSE);
											this.baseMapper.updateById(old);
											this.purchaseFormEntryAlibabaInfoExtMapper.updateById(oldotherext);
										}else {
											PurchaseFormEntryAlibabaInfo newone = new PurchaseFormEntryAlibabaInfo();
											PurchaseFormEntryAlibabaInfoExt newext = new PurchaseFormEntryAlibabaInfoExt();
											newone.setAlibabaOrderid(new BigInteger(alibabaOrderid));
											newone.setAlibabaAuth(new BigInteger(alibabaAuthid));
											newone.setEntryid(entryother.getId());
											
											newext.setAlibabaOrderid(new BigInteger(alibabaOrderid));
											newext.setAlibabaAuth(new BigInteger(alibabaAuthid));
											newext.setEntryid(entryother.getId());
											
											
											newext.setOrderStatus(status);
											newone.setOrderInfo(json);
											newext.setOrderStatus(status);
											newext.setOrderRefreshTime(new Date());
											newext.setLogisticsStatus(Boolean.FALSE);
											newext.setLogisticsTraceStatus(Boolean.FALSE);
											this.baseMapper.insert(newone);
											this.purchaseFormEntryAlibabaInfoExtMapper.insert(newext);
										}
										
									}
							}
							
							}
						}
				}
		   }
		return result;
	}
 
	public void refreshAblibabaDateTask(){
		List<PurchaseFormEntryAlibabaInfo> list=this.baseMapper.findNeedRefresh();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					for(PurchaseFormEntryAlibabaInfo item:list) {
						try {
						   captureAlibabaOrder(null,item.getAlibabaAuth().toString(),item.getAlibabaOrderid().toString(),item.getEntryid());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Thread.sleep(60000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(PurchaseFormEntryAlibabaInfo item:list) {
					try {
						captureLogisticsTraceInfo(null,item.getAlibabaAuth().toString(),item.getAlibabaOrderid().toString(),item.getEntryid());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public JSONObject bindAlibabaOrder(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		// TODO Auto-generated method stub
		 return captureAlibabaOrder(user,alibabaAuthid,alibabaOrderid,purchaseEntryid);
	}

	public Object captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid, String purchaseEntryid) {
		// TODO Auto-generated method stub
		PurchaseFormEntryAlibabaInfo oldone = this.baseMapper.selectById(purchaseEntryid);
		PurchaseFormEntryAlibabaInfoExt oldext = this.purchaseFormEntryAlibabaInfoExtMapper.selectById(purchaseEntryid);
		Map<String,Object> map = new HashMap<String,Object>();
		if(oldext!=null&&oldone!=null&&oldext.getOrderStatus()!=null&&oldext.getOrderStatus().equals("success")) {
			map.put("orderResult",GeneralUtil.getJsonObject(oldone.getOrderInfo()));
			if(oldone.getLogisticsInfo()==null) {
				map.put("orderResult", this.captureAlibabaOrderInfo(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
				map.put("TraceResult", this.captureLogisticsTraceInfo(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
			}else {
				map.put("TraceResult", GeneralUtil.getJsonObject(oldone.getLogisticsTraceInfo())  );
				map.put("LogisticsResult",  GeneralUtil.getJsonObject(oldone.getLogisticsInfo()));
				return map;
			}
		}else {
			map.put("orderResult", this.captureAlibabaOrder(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
			map.put("TraceResult", this.captureLogisticsTraceInfo(user,alibabaAuthid,alibabaOrderid,purchaseEntryid));
		}
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
				oldext.setLogisticsRefreshTime(new Date());
				
				PurchaseFormEntryAlibabaInfoExt newext = new PurchaseFormEntryAlibabaInfoExt();
				newext.setAlibabaOrderid(new BigInteger(alibabaOrderid));
				newext.setAlibabaAuth(new BigInteger(alibabaAuthid));
				newext.setEntryid(purchaseEntryid);
				newext.setLogisticsRefreshTime(new Date());
				
				this.baseMapper.insert(newone);
				this.purchaseFormEntryAlibabaInfoExtMapper.insert(newext);
			}else {
				oldone.setLogisticsInfo(json.toString());
				oldext.setLogisticsRefreshTime(new Date());
				if("success".equals(oldext.getOrderStatus())
						||"cancel".equals(oldext.getOrderStatus())
						||"terminated".equals(oldext.getOrderStatus())
						||"confirm_goods".equals(oldext.getOrderStatus())) {
					oldext.setLogisticsStatus(Boolean.TRUE);
				}
				this.baseMapper.updateById(oldone);
				this.purchaseFormEntryAlibabaInfoExtMapper.updateById(oldext);
			}
		} 
		map.put("LogisticsResult", json);
		return map;
	}

	public Object captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid,
			String purchaseEntryid) {
		// TODO Auto-generated method stub
		PurchaseFormEntryAlibabaInfoExt oldext = this.purchaseFormEntryAlibabaInfoExtMapper.selectById(purchaseEntryid);
		JSONObject json = null;
		json = purchaseAlibabaAuthService.captureLogisticsTraceInfo(user, alibabaAuthid, alibabaOrderid);
		if (json!=null&&json.containsKey("errorMessage")) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				return json;
			}
		}
        if(json!=null) {
        	PurchaseFormEntryAlibabaInfo oldone = this.baseMapper.selectById(purchaseEntryid);
        	oldone.setLogisticsTraceInfo(json.toString());
        	this.baseMapper.updateById(oldone);
    		if("success".equals(oldext.getOrderStatus())
    				||"cancel".equals(oldext.getOrderStatus())
    				||"terminated".equals(oldext.getOrderStatus())
    				||"confirm_goods".equals(oldext.getOrderStatus())) {
    			oldext.setLogisticsTraceRefreshTime(new Date());
    			oldext.setLogisticsTraceStatus(Boolean.TRUE);
    		}
    		if(json.getBooleanValue("success")) {
    			String signinfo=null;
    			Date acceptTime=null;
    			JSONArray logisticsTrace = json.getJSONArray("logisticsTrace");
    			for(int i=0;i<logisticsTrace.size();i++) {
    				JSONObject item = logisticsTrace.getJSONObject(i);
    				JSONArray logisticsSteps=item.getJSONArray("logisticsSteps");
    				for(int j=0;j<logisticsSteps.size();j++) {
    					JSONObject sub = logisticsSteps.getJSONObject(j);
    					if(sub.getString("remark").contains("签收")) {
    						signinfo=sub.getString("remark");
    						if(signinfo!=null&&signinfo.length()>100) {
    							signinfo=signinfo.substring(0,90)+"..."; 
    						}
    						acceptTime=sub.getDate("acceptTime");
    					}
    				}
    			}
    			if(signinfo!=null) {
    				oldext.setSigninfo(signinfo);
    				oldext.setLogisticsTraceStatus(true);
    				oldext.setLogisticsRefreshTime(acceptTime);
    			} 
    			
    		}
    		
    		this.purchaseFormEntryAlibabaInfoExtMapper.updateById(oldext);
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

	@Override
	public void checkPay(BigInteger number,BigDecimal price,String acct) {
		// TODO Auto-generated method stub
			if(number!=null&&price!=null) {
				BigDecimal totalprice = purchaseFormEntryAlibabaInfoExtMapper.getPriceByAlibabaOrder(number.toString(),acct);
				if(totalprice==null||price.doubleValue()!=totalprice.doubleValue()) {
					throw new BizException("系统付款金额为"+(totalprice!=null?totalprice:0)+",与订单金额不匹配");
				}
			}
		
	}

	 
}
