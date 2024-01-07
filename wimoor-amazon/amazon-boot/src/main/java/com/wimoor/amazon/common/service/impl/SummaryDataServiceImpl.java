package com.wimoor.amazon.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.mapper.SummaryDataMapper;
import com.wimoor.amazon.common.pojo.dto.SummaryMutilParameterQueryDTO;
import com.wimoor.amazon.common.pojo.entity.SummaryData;
import com.wimoor.amazon.common.pojo.vo.ChartLine;
import com.wimoor.amazon.common.pojo.vo.ChartMarkpoint;
import com.wimoor.amazon.common.service.ISummaryDataService;
import com.wimoor.amazon.inventory.mapper.InventoryReportHisMapper;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReportHis;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.orders.mapper.OrdersSummaryMapper;
import com.wimoor.amazon.orders.mapper.SummaryAllMapper;
import com.wimoor.amazon.orders.pojo.entity.OrdersSummary;
import com.wimoor.amazon.orders.pojo.vo.ProductSalesRankVo;
import com.wimoor.amazon.util.ChartPoint;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryDataServiceImpl  extends ServiceImpl<SummaryDataMapper, SummaryData> implements ISummaryDataService{

  final SummaryAllMapper summaryAllMapper;
  final IMarketplaceService iMarketplaceService;
  final AmzOrderReturnsMapper amzOrderReturnsMapper;
  final OrdersSummaryMapper ordersSummaryMapper;
  final OrdersReportMapper ordersReportMapper;
  final InventoryReportHisMapper inventoryReportHisMapper;
@Override
public Map<String, Object> selectSumByMutilParameter(SummaryMutilParameterQueryDTO parameter) {
	// TODO Auto-generated method stub
	String marketplaceid=parameter.getMarketplaceid();
	 Marketplace market =null;
	  if(!StrUtil.isEmpty(marketplaceid)) {
		  market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
	  }
	  if(market!=null) {
		  parameter.setSalechannel(market.getPointName());
	  } 
	  return summaryAllMapper.selectSumByMutilParameter(BeanUtil.beanToMap(parameter));
}

@Override
public Map<String, Object> getReturnOrderSummary(SummaryMutilParameterQueryDTO parameter) {
	// TODO Auto-generated method stub
	String marketplaceid=parameter.getMarketplaceid();
	 Marketplace market =null;
	  if(StrUtil.isEmpty(marketplaceid)) {
		  market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
	  }
	  if(market!=null) {
		  parameter.setSalechannel(market.getPointName());
	  } 
	  return amzOrderReturnsMapper.returnsOrderSum(parameter);
}

public Map<String, Object> getReturnOrder(SummaryMutilParameterQueryDTO parameter) {
	 String marketplaceid=parameter.getMarketplaceid();
	 Marketplace market =null;
	  if(StrUtil.isEmpty(marketplaceid)) {
		  market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
	  }
	  List<Map<String,Object>> listMap = null;
	  if(market!=null) {
		  parameter.setSalechannel(market.getPointName());
		  listMap= amzOrderReturnsMapper.returnsOrder(parameter); 
	  }else {
		  listMap= amzOrderReturnsMapper.returnsOrder(parameter);
	  }
	  if(listMap==null)return null;
	  Date beginDate=GeneralUtil.getDatez(parameter.getBeginDate());
	  Date endDate=GeneralUtil.getDatez(parameter.getEndDate());
	  List<String> labels = ChartPoint.getLabels(ChartPoint.SumType.Daily, beginDate, endDate);
	  List<Object> quantityList = ChartPoint.getChartData(ChartPoint.SumType.Daily, listMap, "return_date", "quantity", beginDate, endDate);
	  Map<String,Object> result=new HashMap<String,Object>();
	  result.put("labels", labels);
	  result.put("quantityList", quantityList);
	  return result;
	
}

@Override
public Map<String, Object> selectByMutilParameter(SummaryMutilParameterQueryDTO parameter) {
	 String marketplaceid=parameter.getMarketplaceid();
	 Marketplace market =null;
	  if(!StrUtil.isEmpty(marketplaceid)) {
		  market = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
	  }
	  List<Map<String,Object>> listMap = null;
	  if(market!=null) {
		  parameter.setSalechannel(market.getPointName());
		  listMap= summaryAllMapper.selectByMutilParameter(BeanUtil.beanToMap(parameter)); 
	  }else {
		  listMap= summaryAllMapper.selectByMutilParameter(BeanUtil.beanToMap(parameter));
	  }
	  if(listMap==null)return null;
	  Date beginDate=GeneralUtil.getDatez(parameter.getBeginDate());
	  Date endDate=GeneralUtil.getDatez(parameter.getEndDate());
	  List<String> labels = ChartPoint.getLabels(ChartPoint.SumType.Daily, beginDate, endDate);
	  List<Object> quantityList = ChartPoint.getChartData(ChartPoint.SumType.Daily, listMap, "purchase_date", "quantity", beginDate, endDate);
	  List<Object> orderNumberList = ChartPoint.getChartData(ChartPoint.SumType.Daily, listMap, "purchase_date", "ordernumber", beginDate, endDate);
	  Map<String,Object> result=new HashMap<String,Object>();
	  result.put("labels", labels);
	  result.put("quantityList", quantityList);
	  result.put("orderNumberList", orderNumberList);
	  return result;
}

@Override
public List<Map<String, Object>> marketSummary(SummaryMutilParameterQueryDTO parameter) {
	// TODO Auto-generated method stub
	return summaryAllMapper.marketSummary(BeanUtil.beanToMap(parameter));
}
@Override
public List<ProductSalesRankVo> top5(SummaryMutilParameterQueryDTO parameter) {
	// TODO Auto-generated method stub
	return ordersSummaryMapper.top5(parameter);
}




public ChartLine findOrderSummaryBySku(String groupid,String amazonAuthId, String sku, String marketplaceid,Integer daysize ,String linetype, UserInfo user) {
	SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");
	Map<String, Object> param = new HashMap<String, Object>();
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DATE, daysize*-1-1);
	List<OrdersSummary> list = null;
	param.put("sku", sku);
	param.put("marketplaceid", marketplaceid);
	param.put("purchaseDate", GeneralUtil.getStrDate(c.getTime()));
	param.put("shopid", user.getCompanyid());
	param.put("groupid", groupid);
	param.put("amazonAuthId", amazonAuthId);
	if ("EU".equals(marketplaceid)) {
		list = ordersSummaryMapper.selectBySkuOfEU(param);
	} else {
		list = ordersSummaryMapper.selectBySkuMarketplaceForShip(param);
	}
	Marketplace marketplace = iMarketplaceService.findMapByMarketplaceId().get(marketplaceid);
	List<InventoryReportHis> inventoryList = null;
	String datetime=GeneralUtil.getStrDate(c.getTime());
	param.put("byday", datetime);
	if (marketplace == null && "EU".equals(marketplaceid)) {
		param.put("marketplaceid","EU");
		inventoryList = inventoryReportHisMapper.selectBySkuMarketplace(param);
	} else if (marketplace != null && "EU".equals(marketplace.getRegion())) {
		param.put("marketplaceid","EU");
		inventoryList = inventoryReportHisMapper.selectBySkuMarketplace(param);
	} else {
		inventoryList = inventoryReportHisMapper.selectBySkuMarketplace(param);
	}

	HashMap<String, Integer> inventoryMap = new HashMap<String, Integer>();
	if(inventoryList != null) {
		for (int index = 0; index < inventoryList.size(); index++) {
			inventoryMap.put(inventoryList.get(index).getSku() + sdf1.format(inventoryList.get(index).getByday()),
					inventoryList.get(index).getAfnTotalQuantity());
		}
	}
	List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();

	SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
	Map<String, Object> tempmap = new HashMap<String, Object>();
	if (list != null && list.size() > 0) {
		for (int i = 0; i < list.size(); i++) {
			if(linetype!=null&&linetype.equals("order")) {
				tempmap.put(list.get(i).getSku() + sdf.format(list.get(i).getPurchaseDate()), list.get(i).getOrdersum());
			}else {
				tempmap.put(list.get(i).getSku() + sdf.format(list.get(i).getPurchaseDate()), list.get(i).getQuantity());
			}
			
		}
	}
	 Calendar today=Calendar.getInstance();
	 today.add(Calendar.DATE,-1);
	 String yesterday=sdf.format(today.getTime());
 
		List<Object> myskuData = new ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		int summarySales = 0;
		List<ChartMarkpoint> markpoint = new ArrayList<ChartMarkpoint>();
		String mysku = sku;
		for (int i = 0; i <= daysize; i++, c.add(Calendar.DATE, 1)) {
			String tempkey = sdf.format(c.getTime());
			Integer value = tempmap.get(mysku + tempkey) == null ? Integer.valueOf("0") : Integer.valueOf(tempmap.get(mysku + tempkey).toString());
			if(yesterday.equals(tempkey)&&summarySales>0) {
				param.put("sku", mysku);
				if("EU".equals(marketplaceid)) {
					param.put("marketplaceid", null);
					param.put("region", marketplaceid);
				}else {
					param.put("marketplaceid", marketplaceid);
					param.put("region", null);
				}
				param.put("startDate", GeneralUtil.formatDate(today.getTime()));
				param.put("endDate",  GeneralUtil.formatDate(new Date()));
				Integer mvalue = ordersReportMapper.selectProductOrdersTodayOne(param);
				if(mvalue!=null) {
					value=mvalue;
				}
				
			}
			myskuData.add(value);
			summarySales += value;
			Integer inventoryStatus = inventoryMap.get(mysku + tempkey);
			if ((inventoryStatus == null || inventoryStatus == 0) && (value == null || value == 0)) {
				ChartMarkpoint markpointkeyMap = new ChartMarkpoint();
				markpointkeyMap.setName( tempkey + "库存");
				markpointkeyMap.setValue("0");
				markpointkeyMap.setXAxis(tempkey);
				markpointkeyMap.setYAxis(value);
				markpoint.add(markpointkeyMap);
			}
		 
		}
		listMap.add(map);
		ChartLine chartline=new ChartLine();
		chartline.setMarkpoint(markpoint);
		chartline.setData(myskuData);
		chartline.setName(mysku);
		chartline.setSummary(summarySales);
	return chartline;
}


}
