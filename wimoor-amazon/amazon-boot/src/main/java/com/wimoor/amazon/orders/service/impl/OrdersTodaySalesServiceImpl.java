package com.wimoor.amazon.orders.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.model.fbainventory.InventoryDetails;
import com.amazon.spapi.model.fbainventory.InventorySummary;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.inventory.service.IInventorySupplyService;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.orders.service.IOrdersTodaySalesService;
import com.wimoor.common.GeneralUtil;
@Service
public class OrdersTodaySalesServiceImpl implements IOrdersTodaySalesService{
	@Resource
	OrdersReportMapper ordersReportMapper;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	IInventorySupplyService inventorySupplyService;
	@Override
	public List<Map<String, Object>> getParamOfTodayOrder(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result=new  ArrayList<Map<String, Object>>();
		Map<String, Object> result1 = ordersReportMapper.selectOrderTodaySummary(paramMap);
		String startDate = paramMap.get("startDate").toString();
		Date date1 = GeneralUtil.getDatez(startDate);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		c.setTime(date1);
		c.add(Calendar.DATE, -1);
		paramMap.put("startDate", GeneralUtil.formatDate(c.getTime(), sdf));
		paramMap.put("endDate", startDate);
		Map<String, Object> result2 = ordersReportMapper.selectOrderYesDaySummary(paramMap);
		result.add(result1);
		result.add(result2);
		return result;
	}
	

	public void setProductSalesTodayExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("groupname", "店铺");
		titlemap.put("market", "站点");
		List<Map<String, Object>> list = null;
		Map<String, InventorySummary> invMap =null;
			titlemap.put("currency", "币别");
			titlemap.put("QuantityOrdered", "今日销量");
			titlemap.put("totalprice", "今日销售额");
			titlemap.put("price", "今日单价");
			titlemap.put("QuantityOrdered_yy", "昨日销量");
			titlemap.put("totalprice_yy", "昨日销售额");
			titlemap.put("price_yy", "昨日单价");
			titlemap.put("QuantityOrdered_lw", "上周同日销量");
			titlemap.put("totalprice_lw", "上周同日销售额");
			titlemap.put("price_lw", "上周同日单价");
			titlemap.put("instock", "可售库存");
			titlemap.put("inbound", "待收货库存");
			titlemap.put("transfer", "预留库存");
			list = ordersReportMapper.selectProductOrdersTodayList(param);
			if (list != null && list.size() > 0) {
				AmazonAuthority amazonAuthority = amazonAuthorityService.selectByGroupAndMarket(
						param.get("groupid").toString(), param.get("marketplaceid").toString());
				Marketplace marketplace = (Marketplace) param.get("market");
				amazonAuthority.setMarketPlace(marketplace);
				List<String> skulist=new ArrayList<String>();
				for(Map<String, Object> item:list) {
					skulist.add(item.get("sku").toString());
				}
			    invMap = inventorySupplyService.captureInventorySupply(amazonAuthority, skulist);
			}
	 
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i + 1);
			Map<String, Object> map = list.get(i);
		    param.put("sku", map.get("sku"));
		    InventoryDetails detail =null;
			if(invMap!=null) {
				InventorySummary sumData = invMap.get(map.get("sku").toString());
				if(sumData!=null) {
					detail=sumData.getInventoryDetails();
				}
			}	
			for (int j = 0; j < titlearray.length; j++) {
				Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = map.get(titlearray[j].toString());
				if(value==null && "currency".equals(titlearray[j].toString())){
					value = ((Marketplace)param.get("market")).getCurrency();
				}
				if (value != null) {
					if(titlearray[j].toString().contains("price")){
						value = GeneralUtil.formatterQuantity((BigDecimal) value);
					}
					cell.setCellValue(value.toString());
				} else {
					 
					if(detail!=null) {
						if(titlearray[j].toString().equals("instock")){
							value=detail.getFulfillableQuantity();
						}
						else if(titlearray[j].toString().equals("inbound")){
							Integer rec = detail.getInboundReceivingQuantity();
							Integer ship=detail.getInboundShippedQuantity();
							Integer work=detail.getInboundWorkingQuantity();
							Integer inboud=rec+ship+work;
							value = inboud;
						}
						else if(titlearray[j].toString().equals("transfer")&&detail.getReservedQuantity()!=null){
							value=detail.getReservedQuantity().getTotalReservedQuantity();
						} else {
							value="--";
						}
						cell.setCellValue(value.toString()); 
					}else {
						cell.setCellValue("--");
					}
				
				}
			}
		}
	}

	@Override
	public IPage<Map<String, Object>> selectOrderTodayList(Page<Object> page, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return ordersReportMapper.selectProductOrdersTodayList(page, paramMap);
	}

}
