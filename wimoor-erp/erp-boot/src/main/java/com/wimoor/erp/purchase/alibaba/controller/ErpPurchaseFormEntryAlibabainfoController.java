package com.wimoor.erp.purchase.alibaba.controller;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.assembly.service.IAssemblyFormEntryService;
import com.wimoor.erp.assembly.service.IAssemblyService;
import com.wimoor.erp.material.pojo.entity.MaterialSupplier;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.IMaterialSupplierService;
import com.wimoor.erp.material.service.IStepWisePriceService;
import com.wimoor.erp.purchase.alibaba.mapper.PurchaseFormEntryLogisticsMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.ErpPurchaseAlibabaContact;
import com.wimoor.erp.purchase.alibaba.pojo.entity.ErpPurchaseAlibabaProductitems;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfo;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryLogistics;
import com.wimoor.erp.purchase.alibaba.service.IErpPurchaseAlibabaContactService;
import com.wimoor.erp.purchase.alibaba.service.IErpPurchaseAlibabaProductitemsService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseFormEntryAlibabaInfoService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormEntry;
import com.wimoor.erp.purchase.service.IPurchaseFormEntryService;
import com.wimoor.erp.purchase.service.IPurchaseFormService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-03-16
 */
@Api(tags = "采购单接口")
@RestController
@SystemControllerLog( "采购单1688接口")
@RequestMapping("/api/v1/purchase/alibaba/entry/alibabainfo")
@RequiredArgsConstructor
public class ErpPurchaseFormEntryAlibabainfoController {
	@Autowired
	IPurchaseFormEntryAlibabaInfoService purchaseFormEntryAlibabaInfoService;
	final IPurchaseAlibabaAuthService purchaseAlibabaAuthService;
	final IPurchaseFormEntryService iPurchaseFormEntryService;
	final IMaterialService iMaterialService;
	final IMaterialSupplierService iMaterialSupplierService;
	final IErpPurchaseAlibabaProductitemsService iErpPurchaseAlibabaProductitemsService;
	final PurchaseFormEntryLogisticsMapper purchaseFormEntryLogisticsMapper;
	final IErpPurchaseAlibabaContactService iErpPurchaseAlibabaContactService;
	@GetMapping("/catchLogisticsInfo")
	public Result<Object> catchLogisticsInfoAction(String purchaseEntryid) throws BizException {
		UserInfo userinfo = UserInfoContext.get();
		PurchaseFormEntryAlibabaInfo resultObj = purchaseFormEntryAlibabaInfoService.getEntryAlibabainfo(purchaseEntryid);
		if(resultObj==null) {
			return Result.success();
		}
		String alibabaAuthid=resultObj.getAlibabaAuth().toString();
		String alibabaOrderid =resultObj.getAlibabaOrderid().toString();
		return Result.success(purchaseFormEntryAlibabaInfoService.captureLogisticsInfo(userinfo, alibabaAuthid, alibabaOrderid, purchaseEntryid));
	}
 
	@GetMapping("/bindAlibabaOrder")
	public Result<Boolean> bindAlibabaOrderAction(String alibabaAuthid,
			String alibabaOrderid,
			String purchaseEntryid) throws BizException {
		UserInfo userinfo = UserInfoContext.get();
		if(StrUtil.isNotBlank(alibabaOrderid)) {
			alibabaOrderid=alibabaOrderid.trim();
		}else {
			throw new BizException("订单号不能为空");
		}
		purchaseFormEntryAlibabaInfoService.bindAlibabaOrder(userinfo, alibabaAuthid, alibabaOrderid, purchaseEntryid);
		return Result.success();
	}
	
	@GetMapping("/unbindAlibabaOrder")
	public Object unbindAlibabaOrderAction(String purchaseEntryid) throws BizException {
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(purchaseFormEntryAlibabaInfoService.unbindAlibabaOrder(userinfo, purchaseEntryid));
	}
	
	@GetMapping("/getAlibabaOrder")
	public Result<Map<String,Object>> getAlibabaOrderAction(String purchaseEntryid) throws BizException {
		PurchaseFormEntryAlibabaInfo resultObj = purchaseFormEntryAlibabaInfoService.getEntryAlibabainfo(purchaseEntryid);
		Map<String, Object> result = BeanUtil.beanToMap(resultObj);
		if(result!=null&&result.get("orderInfo")!=null) {
			String orderInfo = result.get("orderInfo").toString();
			JSONObject orderinfo = GeneralUtil.getJsonObject(orderInfo);
			JSONObject resultjson=orderinfo.getJSONObject("result");
			JSONArray  productItems=resultjson.getJSONArray("productItems");
			PurchaseFormEntry entry = iPurchaseFormEntryService.getById(purchaseEntryid);
			MaterialSupplier supplier = iMaterialSupplierService.selectSupplier(entry.getMaterialid(),entry.getSupplier());
			if(supplier!=null) {
				for(int i =0 ;i<productItems.size();i++) {
					JSONObject product = productItems.getJSONObject(i);
					String specId=product.getString("specId");
					String productID=product.getString("productID");
					if(specId.equals(supplier.getSpecid())&&productID.equals(supplier.getProductcode())) {
						 product.put("isbind","true");
					}
				}
			}
			result.put("orderInfo",orderinfo);
		}
		return Result.success(result);
	}
	
	@GetMapping("/addFeedback")
	public Result<JSONObject> addFeedbackAction(String alibabaAuthid,
			String alibabaOrderid,
			String remark) throws BizException {
		JSONObject result = purchaseAlibabaAuthService.addFeedback(alibabaAuthid,alibabaOrderid,remark);
		return Result.success(result);
	}
	
	@GetMapping("/cancelOrder")
	public Result<JSONObject> cancelOrderAction(String alibabaAuthid,
			String alibabaOrderid,
			String cancelReason,
			String remark) throws BizException {
		JSONObject result = purchaseAlibabaAuthService.cancelOrder(alibabaAuthid,alibabaOrderid,cancelReason,remark);
		return Result.success(result);
	}
	
	@GetMapping("/productInfoByEntry")
	public Result<JSONObject> productInfoAction(String alibabaAuthid, String purchaseEntryid ) throws BizException {
		PurchaseFormEntry entry = iPurchaseFormEntryService.getById(purchaseEntryid);
		MaterialSupplier supplier = iMaterialSupplierService.selectSupplier(entry.getMaterialid(),entry.getSupplier());
		if(supplier.getProductcode()!=null) {
			JSONObject result = purchaseAlibabaAuthService.productInfo(alibabaAuthid,supplier.getProductcode());
			return Result.success(result);
		}else {
			JSONObject result=new JSONObject();
			result.put("error", "未找到供应商编码");
			return Result.success(result);
		}
	}
	
	@GetMapping("/getBindProductInfoByEntry")
	public Result<ErpPurchaseAlibabaProductitems> getBindProductInfoAction( String purchaseEntryid ) throws BizException {
				PurchaseFormEntry entry = iPurchaseFormEntryService.getById(purchaseEntryid);
				MaterialSupplier supplier = iMaterialSupplierService.selectSupplier(entry.getMaterialid(),entry.getSupplier());
				LambdaQueryWrapper<ErpPurchaseAlibabaProductitems> query=new LambdaQueryWrapper<ErpPurchaseAlibabaProductitems>();
				query.eq(ErpPurchaseAlibabaProductitems::getSpecId, supplier.getSpecid());
				query.eq(ErpPurchaseAlibabaProductitems::getProductID, supplier.getProductcode());
				ErpPurchaseAlibabaProductitems item = iErpPurchaseAlibabaProductitemsService.getOne(query);
			return Result.success(item);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/bindProductInfoByEntry")
	public Result<JSONObject> bindProductInfoAction(@RequestBody Map<String,Object> param) throws BizException {
			// JSONObject result =null;
			 String alibabaAuthid =param.get("alibabaAuthid")!=null?param.get("alibabaAuthid").toString():null;
			 String purchaseEntryid=param.get("purchaseEntryid")!=null?param.get("purchaseEntryid").toString():null;
			 Map<String,Object> productInfo=param.get("productInfo")!=null?BeanUtil.beanToMap(param.get("productInfo")):null;
			 Map<String,Object> orderInfo=param.get("orderInfo")!=null?BeanUtil.beanToMap(param.get("orderInfo")):null;
			 String productCode=productInfo.get("productID")!=null?productInfo.get("productID").toString():null;
			 String specId=productInfo.get("specId")!=null?productInfo.get("specId").toString():null;
//			 try {
//				 result= purchaseAlibabaAuthService.syncProductListPushed(alibabaAuthid,productCode);
//				 
//			 }catch(Exception e) {
//				 e.printStackTrace();
//			 }
//			 if(result!=null&&result.getJSONObject("result").getBooleanValue("success")==true) {
//	             System.out.println(result);   
//			 }
			 
			PurchaseFormEntry entry = iPurchaseFormEntryService.getById(purchaseEntryid);
			MaterialSupplier supplier = iMaterialSupplierService.selectSupplier(entry.getMaterialid(),entry.getSupplier());
			Map<String,Object> baseInfo=(Map<String, Object>)orderInfo.get("baseInfo");
			Map<String,Object> sellerContact=(Map<String, Object>)baseInfo.get("sellerContact");
			String sellerID=baseInfo.get("sellerID").toString();
			LambdaQueryWrapper<ErpPurchaseAlibabaContact> querycontact=new LambdaQueryWrapper<ErpPurchaseAlibabaContact>();
			querycontact.eq(ErpPurchaseAlibabaContact::getId, sellerID);
			querycontact.eq(ErpPurchaseAlibabaContact::getCustomer, entry.getSupplier());
			ErpPurchaseAlibabaContact contact = iErpPurchaseAlibabaContactService.getOne(querycontact);
			String imInPlatform=null;
			String phone=null;
			String companyName=null;
			String mobile=null;
			String cname=null;
			String email=null;
			if(sellerContact!=null) {
				imInPlatform=sellerContact.get("imInPlatform")!=null?sellerContact.get("imInPlatform").toString():null;
				phone=sellerContact.get("phone")!=null?sellerContact.get("phone").toString():null;
				companyName=sellerContact.get("companyName")!=null?sellerContact.get("companyName").toString():null;
				mobile=sellerContact.get("mobile")!=null?sellerContact.get("mobile").toString():null;
				cname=sellerContact.get("name")!=null?sellerContact.get("name").toString():null;
				email=sellerContact.get("email")!=null?sellerContact.get("email").toString():null;
			}
			if(contact==null) {
		            contact=new ErpPurchaseAlibabaContact();
		            contact.setId(sellerID);
		            contact.setCustomer( entry.getSupplier());
				    contact.setMobile(mobile);
				    contact.setCompanyName(companyName);
				    contact.setEmail(email);
				    contact.setImInPlatform(imInPlatform);
				    contact.setPhone(phone);
				    contact.setName(cname);
				    iErpPurchaseAlibabaContactService.save(contact);
			}else {
				contact=new ErpPurchaseAlibabaContact();
	            contact.setId(sellerID);
	            contact.setCustomer( entry.getSupplier());
			    contact.setMobile(mobile);
			    contact.setCompanyName(companyName);
			    contact.setEmail(email);
			    contact.setImInPlatform(imInPlatform);
			    contact.setPhone(phone);
			    contact.setName(cname);
			    iErpPurchaseAlibabaContactService.update(contact,querycontact);
			}
			
			LambdaQueryWrapper<ErpPurchaseAlibabaProductitems> query=new LambdaQueryWrapper<ErpPurchaseAlibabaProductitems>();
			query.eq(ErpPurchaseAlibabaProductitems::getSpecId, specId);
			query.eq(ErpPurchaseAlibabaProductitems::getProductID, productCode);
			ErpPurchaseAlibabaProductitems item = iErpPurchaseAlibabaProductitemsService.getOne(query);
			boolean isnew=false;
			if(item==null) {
				item=new ErpPurchaseAlibabaProductitems();
				item.setSpecId(specId);
				item.setProductID(new BigInteger(productCode));
				isnew=true;
			}
			String productSnapshotUrl=productInfo.get("productSnapshotUrl").toString();
			List<Object> images = (List<Object>)productInfo.get("productImgUrl");
			String productImgUrl=images.get(0).toString();
			String name=productInfo.get("name").toString();
			String unit=productInfo.get("unit").toString();
			BigDecimal price=new BigDecimal(productInfo.get("price").toString());
			String skuinfo="";
			List<Object> skuinfoarray = (List<Object>)productInfo.get("skuInfos");
			if(skuinfoarray!=null&&skuinfoarray.size()>0) {
				for(int j =0;j<skuinfoarray.size();j++) {
					 Map<String,Object> infoattr=(Map<String, Object>) skuinfoarray.get(j);
					if(skuinfo.equals("")) {
						skuinfo=skuinfo+infoattr.get("name")+":"+infoattr.get("value");
					}else {
						skuinfo=skuinfo+","+infoattr.get("name")+":"+infoattr.get("value");
					}
					
				}
			}
			item.setPrice(price);
			item.setName(name);
			item.setProductImgUrl(productImgUrl);
			item.setProductSnapshotUrl(productSnapshotUrl);
			item.setUnit(unit);
			item.setSkuInfos(skuinfo);
			item.setOfferid(sellerID);
			if(isnew) {
				iErpPurchaseAlibabaProductitemsService.save(item);
			}else {
				iErpPurchaseAlibabaProductitemsService.update(item, query);
			}
			supplier.setProductcode(productCode);
			supplier.setSpecid(specId);
			iMaterialSupplierService.updateById(supplier);
			//}
			return Result.success();
	}
	
	@PostMapping("/preview")
	public Result<JSONObject> previewAction(@RequestBody Map<String,Object> param) throws BizException {
			JSONObject result = purchaseFormEntryAlibabaInfoService.preview(param);
			return Result.success(result);
	}
	
	@PostMapping("/createCrossOrder")
	public Result<JSONObject> createCrossOrderAction(@RequestBody Map<String,Object> param) throws BizException {
			JSONObject result = purchaseFormEntryAlibabaInfoService.createCrossOrder(param);
			return Result.success(result);
	}
	
	@GetMapping("/payWay")
	public Result<JSONObject> payWayAction(String alibabaAuthid, String alibabaOrderid) throws BizException {
			JSONObject result = purchaseFormEntryAlibabaInfoService.payWay(alibabaAuthid,alibabaOrderid);
			return Result.success(result);
	}
	
	@PostMapping("/payUrl")
	public Result<JSONObject> payUrl(@RequestBody Map<String,Object> param) throws BizException {
			JSONObject result = purchaseFormEntryAlibabaInfoService.payUrl(param);
			return Result.success(result);
	}
	
	@PostMapping("/getRefundReasonList")
	public Result<JSONObject> getRefundReasonList(@RequestBody Map<String,Object> param) throws BizException {
			JSONObject result = purchaseFormEntryAlibabaInfoService.getRefundReasonList(param);
			return Result.success(result);
	}
	
	@PostMapping("/createRefund")
	public Result<JSONObject> createRefund(@RequestBody Map<String,Object> param) throws BizException {
			JSONObject result = purchaseFormEntryAlibabaInfoService.createRefund(param);
			return Result.success(result);
	}
	
	@GetMapping("/bindLogisticsId")
	public Result<PurchaseFormEntryLogistics> bindLogisticsIdAction(String purchaseEntryid,String logisticsId) throws BizException {
		    PurchaseFormEntryLogistics logistics = purchaseFormEntryLogisticsMapper.selectById(purchaseEntryid);
		    if(StrUtil.isBlankOrUndefined(logisticsId)||StrUtil.isBlankOrUndefined(purchaseEntryid)) {
		    	throw new BizException("请输入运单号");
		    }
		    if(logistics!=null) {
		    	logistics.setLogisticsid(logisticsId);
		    	logistics.setRefreshtime(new Date());
		    	purchaseFormEntryLogisticsMapper.updateById(logistics);
	    		return Result.success(logistics);
	    	}else {
	    		logistics=new PurchaseFormEntryLogistics();
	    		logistics.setLogisticsid(logisticsId);
	    		logistics.setEntryid(purchaseEntryid);
	    		logistics.setRefreshtime(new Date());
	    		purchaseFormEntryLogisticsMapper.insert(logistics);
	    		return Result.success(logistics);
	    	} 
	}
	
	@GetMapping("/getLogisticsId")
	public Result<String> getLogisticsIdAction(String purchaseEntryid) throws BizException {
	    	PurchaseFormEntryLogistics logistics = purchaseFormEntryLogisticsMapper.selectById(purchaseEntryid);
	    	if(logistics!=null) {
	    		return Result.success(logistics.getLogisticsid());
	    	}else {
	    		return Result.success("");
	    	}
	}
	
}

