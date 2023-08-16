package com.wimoor.amazon.inventory.service.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.FbaInventoryApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fbainventory.GetInventorySummariesResponse;
import com.amazon.spapi.model.fbainventory.GetInventorySummariesResult;
import com.amazon.spapi.model.fbainventory.InventoryDetails;
import com.amazon.spapi.model.fbainventory.InventorySummaries;
import com.amazon.spapi.model.fbainventory.InventorySummary;
import com.amazon.spapi.model.fbainventory.Pagination;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.inventory.mapper.AmzInventoryCountryReportMapper;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

@Service
public class InventorySupplyServiceImpl implements IInventorySupplyService{
	@Autowired
	ApiBuildService apiBuildService;
	@Resource
	private IProductInfoService iProductInfoService;
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	@Resource
	private IProductInOptService iProductInOptService;
	@Resource
	AmzInventoryCountryReportMapper amzInventoryCountryReportMapper;
 
	@Override
	public Map<String, InventorySummary> captureInventorySupplyNew(AmazonAuthority amazonAuthority, Date date) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getInventorySummaries");
		FbaInventoryApi api = apiBuildService.getFbaInventoryApi(amazonAuthority);
		Marketplace market = amazonAuthority.getMarketPlace();
		Map<String,InventorySummary> result=new HashMap<String,InventorySummary>();
		try {
			Calendar c=Calendar.getInstance();
			c.setTime(date);
			GetInventorySummariesResponse response = api.getInventorySummaries("Marketplace", market.getMarketplaceid(), Arrays.asList(market.getMarketplaceid()), true, AmzDateUtils.getOffsetDateTimeUTC(c.getTime()), null, null);
			GetInventorySummariesResult payload = response.getPayload();
			Pagination page = response.getPagination();
			String nexttoken = page.getNextToken();
			Map<String,InventorySummary> itemlist=handlerFbaInventory(amazonAuthority,market,payload.getInventorySummaries());
			result.putAll(itemlist);
			while(nexttoken!=null) {
				     response = api.getInventorySummaries("Marketplace", market.getMarketplaceid(), null, null, null, null, nexttoken);
				     if(response!=null) {
						 payload = response.getPayload();
						 if(response.getPagination()!=null) {
							 page = response.getPagination();
							 if(page!=null) {
								 nexttoken = page.getNextToken();
							 }else {
								 nexttoken=null; 
							 }
						 }else {
							 nexttoken=null;  
						 }
						 if(payload.getInventorySummaries()!=null) {
							 itemlist=handlerFbaInventory(amazonAuthority,market,payload.getInventorySummaries());
							 result.putAll(itemlist);
						 }
						
					 }
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	String handlerInventorySummariesResult(AmazonAuthority amazonAuthority,Marketplace market,GetInventorySummariesResponse response,Map<String,InventorySummary> result){
		 String nexttoken=null;
		 if(response!=null) {
			 GetInventorySummariesResult payload = response.getPayload();
			 if(response.getPagination()!=null) {
				 Pagination page = response.getPagination();
				 if(page!=null) {
					 nexttoken = page.getNextToken();
				 }else {
					 nexttoken=null; 
				 }
			 }else {
				 nexttoken=null;  
			 }
			 if(payload.getInventorySummaries()!=null) {
				 Map<String,InventorySummary> itemlist=handlerFbaInventory(amazonAuthority,market,payload.getInventorySummaries());
				 result.putAll(itemlist);
			 }
		 }
		 return nexttoken;
	}
	
	public Map<String,InventorySummary> captureInventorySupply(AmazonAuthority amazonAuthority,List<String> skulist) {
		Map<String,InventorySummary> result=new HashMap<String,InventorySummary>();
		int totalRows = skulist.size();
		if (totalRows > 50) {
			int totalPages = totalRows / 50;
			if (totalRows % 50 != 0) {
				totalPages = totalPages + 1;
			}
			for (int page = 1; page <= totalPages; page++) {
				List<String> tempskulist = GeneralUtil.getListWithLimit(skulist, page, 50);
				Map<String,InventorySummary>  pageResult=captureInventorySupplyNew(amazonAuthority, tempskulist);
				result.putAll(pageResult);
			}
		} else {
			Map<String,InventorySummary>  pageResult=captureInventorySupplyNew(amazonAuthority, skulist);
			result.putAll(pageResult);
		}
		return result;
	}
	
	@Override
	public Map<String, InventorySummary> captureInventorySupplyNew(AmazonAuthority amazonAuthority, List<String> list) {
		// TODO Auto-generated method stub
		amazonAuthority.setUseApi("getInventorySummaries");
		FbaInventoryApi api = apiBuildService.getFbaInventoryApi(amazonAuthority);
		Marketplace market = amazonAuthority.getMarketPlace();
		Map<String,InventorySummary> result=new HashMap<String,InventorySummary>();
		
		try {
			GetInventorySummariesResponse response = api.getInventorySummaries("Marketplace", market.getMarketplaceid(), Arrays.asList(market.getMarketplaceid()), true, null, list, null);
			 
			String nexttoken = handlerInventorySummariesResult(amazonAuthority,market,response,result);
  
			while(nexttoken!=null) {
				     response = api.getInventorySummaries("Marketplace", market.getMarketplaceid(), null, null, null, null, nexttoken);
				     nexttoken = handlerInventorySummariesResult(amazonAuthority,market,response,result);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	private Map<String, InventorySummary> handlerFbaInventory(AmazonAuthority amazonAuthority, Marketplace market,
			InventorySummaries inventorySummaries) {
		// TODO Auto-generated method stub
		Map<String,InventorySummary> result=new HashMap<String,InventorySummary>();
		for(int i=0;i<inventorySummaries.size();i++) {
			InventorySummary item = inventorySummaries.get(i);
			result.put(item.getSellerSku(), item);
		}
		
		return result;
	}

	
	    public List<ProductInventoryVo> findFBA(String groupid,
										 String marketplaceid,
										 String sku,
										 String myself,
										 String shopid){
	    	HashMap<String, Object> param = new HashMap<String,Object>();
	    	param.put("groupid", groupid);
	    	param.put("marketplaceid", marketplaceid);
	    	param.put("sku", sku);
	    	param.put("myself", myself);
	    	param.put("shopid", shopid);
		return inventoryReportMapper.findFBA(param);
	}
	    
	@Override
	public InventoryReport syncInventorySupply(AmazonAuthority amazonAuthority, List<String> list) {
		if(amazonAuthority!=null) {
			InventoryReport report =null;
			 Map<String, InventorySummary> result = captureInventorySupplyNew(amazonAuthority, list);
			 for(Entry<String, InventorySummary> entry:result.entrySet()) {
				 InventorySummary inv=entry.getValue();
				 InventoryDetails invdetail = inv.getInventoryDetails();
				 if(invdetail!=null) {
					 List<ProductInfo> infolist = iProductInfoService.selectBySku(inv.getSellerSku(),amazonAuthority.getMarketPlace().getMarketplaceid() , amazonAuthority.getId());
					 if(infolist!=null&&infolist.size()>0) {
						 ProductInfo info = infolist.get(0);
						 report = inventoryReportMapper.findbyProductId(info.getId());
					     if(report==null) {
					    	 report= new InventoryReport();
					    	 report.setSku(inv.getSellerSku());
					    	 report.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
					    	 report.setAsin(info.getAsin());
					    	 report.setIsnewest(true);
					    	 report.setAmazonAuthId(amazonAuthority.getId());
					    	 report.setByday(new Date());
					     }
						 report.setAfnFulfillableQuantity(invdetail.getFulfillableQuantity());
						 if(invdetail.getUnfulfillableQuantity()!=null) {
							 report.setAfnUnsellableQuantity(invdetail.getUnfulfillableQuantity().getTotalUnfulfillableQuantity());
						 } 
						 report.setAfnInboundReceivingQuantity(invdetail.getInboundReceivingQuantity());
						 report.setAfnInboundShippedQuantity(invdetail.getInboundShippedQuantity());
						 report.setAfnInboundWorkingQuantity(invdetail.getInboundWorkingQuantity());
						 if(invdetail.getReservedQuantity()!=null) {
							 report.setAfnReservedQuantity(invdetail.getReservedQuantity().getTotalReservedQuantity());
						 }
						 if(invdetail.getResearchingQuantity()!=null) {
							 report.setAfnResearchingQuantity(invdetail.getResearchingQuantity().getTotalResearchingQuantity());
						 }
						 report.setAfnTotalQuantity(inv.getTotalQuantity());
						 report.setIsnewest(true);
						 if(report.idIsNULL()) {
							 inventoryReportMapper.insert(report);
						 }else {
							 report.setByday(new Date());
							 inventoryReportMapper.updateById(report);
						 }
					 }
				    
				 }
			 }
			 return report;
		}else {
			 return null;
		}
		
	}

	@Override
	public List<AmzInventoryCountryReport> findEUFBA(String authid, String sku) {
		LambdaQueryWrapper<AmzInventoryCountryReport> queryWrapper=new LambdaQueryWrapper<AmzInventoryCountryReport>();
		queryWrapper.eq(AmzInventoryCountryReport::getAuthid, new BigInteger(authid));
		queryWrapper.eq(AmzInventoryCountryReport::getSku, sku);
		List<AmzInventoryCountryReport> list = amzInventoryCountryReportMapper.selectList(queryWrapper);
		return list;
	}
	 
}
