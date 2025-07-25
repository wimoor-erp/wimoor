package com.wimoor.amazon.orders.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.annotation.Resource;

import com.wimoor.amazon.util.ChartPoint;
import com.wimoor.common.GeneralUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.entity.OrdersReport;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;
import com.wimoor.amazon.orders.service.IOrderReturnService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;

import cn.hutool.core.util.StrUtil;

@Service("orderReturnService")
public class OrderReturnServiceImpl implements IOrderReturnService{
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	OrdersReportMapper ordersReportMapper;
	@Resource
	AmazonGroupMapper amazonGroupMapper;
	@Resource
	AmzOrderReturnsMapper amzOrderReturnsMapper;
	@Resource
	ProductInfoMapper productInfoMapper;
	@Resource
	IPictureService pictureService;
	@Resource
	IMarketplaceService marketplaceService;
	@Override
	public IPage<AmazonOrdersReturnVo> selectReturnsList(AmazonOrdersReturnDTO condition) {
		 AmazonAuthority auth = amazonAuthorityService.getById(condition.getAmazonauthid());
		 if(auth!=null) {
			 condition.setSellerid(auth.getSellerid());
		 }
		 String gname="";
		 AmazonGroup amzgroup = amazonGroupMapper.selectById(condition.getGroupid());
		 if(amzgroup!=null) {
			 gname=amzgroup.getName();
		 }
		 if(condition.getSearchtype()!=null&&condition.getSearchtype().equals("sku")) {
			 if(StrUtil.isNotBlank(condition.getSearch())) {
				 condition.setSearch("%"+condition.getSearch().trim()+"%");
			 }
		 }
		 if(condition.getSku()!=null){
			 condition.setSearch(condition.getSku());
			 condition.setSearchtype("sku");
		 }
        return amzOrderReturnsMapper.selectReturnsList(condition.getPage(),condition);
	}

	public IPage<Map<String,Object>> selectReturnsListBySKU(AmazonOrdersReturnDTO condition) {
		AmazonAuthority auth = amazonAuthorityService.getById(condition.getAmazonauthid());
		if(auth!=null) {
			condition.setSellerid(auth.getSellerid());
		}
		String gname="";
		AmazonGroup amzgroup = amazonGroupMapper.selectById(condition.getGroupid());
		if(amzgroup!=null) {
			gname=amzgroup.getName();
		}
		if(condition.getSearchtype()!=null&&condition.getSearchtype().equals("sku")) {
			if(StrUtil.isNotBlank(condition.getSearch())) {
				condition.setSearch("%"+condition.getSearch().trim()+"%");
			}
		}
		if(condition.getSku()!=null){
			condition.setSearch(condition.getSku());
			condition.setSearchtype("sku");
		}
		IPage<Map<String, Object>> list = amzOrderReturnsMapper.selectReturnsListBySKU(condition.getPage(), condition);
		return list;
	}
	public Map<String,Object> selectReturnsSummaryByDay(AmazonOrdersReturnDTO condition) {
		AmazonAuthority auth = amazonAuthorityService.getById(condition.getAmazonauthid());
		if(auth!=null) {
			condition.setSellerid(auth.getSellerid());
		}
		String gname="";
		AmazonGroup amzgroup = amazonGroupMapper.selectById(condition.getGroupid());
		if(amzgroup!=null) {
			gname=amzgroup.getName();
		}
		if(condition.getSearchtype()!=null&&condition.getSearchtype().equals("sku")) {
			if(StrUtil.isNotBlank(condition.getSearch())) {
				condition.setSearch("%"+condition.getSearch().trim()+"%");
			}
		}
		if(condition.getSku()!=null){
			condition.setSearch(condition.getSku());
			condition.setSearchtype("sku");
		}
		List<Map<String, Object>> list = amzOrderReturnsMapper.selectReturnsSummaryByDay(condition);
		List<Map<String, Object>> list2 =amzOrderReturnsMapper.selectOrderSummaryByDay(condition);
		List<String> label = ChartPoint.getLabels(ChartPoint.Daily, GeneralUtil.getDatez(condition.getStartDate()), GeneralUtil.getDatez(condition.getEndDate()));
		List<Object> returnqty = ChartPoint.getChartData(ChartPoint.Daily, list, "returnDate", "quantity", GeneralUtil.getDatez(condition.getStartDate()), GeneralUtil.getDatez(condition.getEndDate()));
		List<Object> returnItemPrice = ChartPoint.getChartData(ChartPoint.Daily, list, "returnDate", "itemPrice", GeneralUtil.getDatez(condition.getStartDate()), GeneralUtil.getDatez(condition.getEndDate()));
		List<Object> quantity = ChartPoint.getChartData(ChartPoint.Daily, list2, "purchaseDate", "quantity", GeneralUtil.getDatez(condition.getStartDate()), GeneralUtil.getDatez(condition.getEndDate()));
		Map<String,Object> chart=new HashMap<>();
		chart.put("label", label);
		chart.put("returnqty", returnqty);
		chart.put("quantity", quantity);
		chart.put("returnItemPrice", returnItemPrice);
		chart.put("returnqtySummary", getSummary(returnqty));
		chart.put("quantitySummary", getSummary(quantity));
		BigDecimal returnqtySummary = new BigDecimal(chart.get("returnqtySummary").toString());
		BigDecimal quantitySummary = new BigDecimal(chart.get("quantitySummary").toString());
		List<Object> rateqty=new ArrayList<Object>();
		for(int i=0;i<returnqty.size();i++) {
			BigDecimal qty = new BigDecimal(quantity.get(i).toString());
			if(qty.floatValue()<0.00001){
				rateqty.add(0);
			}else{
				rateqty.add(new BigDecimal(returnqty.get(i).toString()).divide(qty, 2,RoundingMode.HALF_DOWN));
			}
		}
		chart.put("rateqty",rateqty);
		if(quantitySummary.floatValue()<0.00001){
			chart.put("rateSummary",0);
		}else{
			chart.put("rateSummary",returnqtySummary.multiply(new BigDecimal(100)).divide(quantitySummary, 2,RoundingMode.HALF_DOWN));
		}

		chart.put("returnItemPriceSummary", getSummary(returnItemPrice));
		return chart;
	}
	public List<Map<String, Object>> selectReturnsSummaryByType(AmazonOrdersReturnDTO condition) {
		AmazonAuthority auth = amazonAuthorityService.getById(condition.getAmazonauthid());
		if(auth!=null) {
			condition.setSellerid(auth.getSellerid());
		}
		String gname="";
		AmazonGroup amzgroup = amazonGroupMapper.selectById(condition.getGroupid());
		if(amzgroup!=null) {
			gname=amzgroup.getName();
		}
		if(condition.getSearchtype()!=null&&condition.getSearchtype().equals("sku")) {
			if(StrUtil.isNotBlank(condition.getSearch())) {
				condition.setSearch("%"+condition.getSearch().trim()+"%");
			}
		}
		if(condition.getSku()!=null){
			condition.setSearch(condition.getSku());
			condition.setSearchtype("sku");
		}
		List<Map<String, Object>> list = amzOrderReturnsMapper.selectReturnsSummaryByType( condition);
		return list;
	}
	BigDecimal getSummary(List<Object> list){
		BigDecimal summary=new BigDecimal("0");
		for(int i=0;i<list.size();i++) {
			summary=summary.add(new BigDecimal(list.get(i).toString()));
		}
		return summary;
	}

	@Override
	public void downloadReturnlist(SXSSFWorkbook workbook,AmazonOrdersReturnDTO condition) {
		AmazonAuthority auth = amazonAuthorityService.getById(condition.getAmazonauthid());
		 if(auth!=null) {
			 condition.setSellerid(auth.getSellerid());
		 }
		 String gname="";
		 AmazonGroup amzgroup = amazonGroupMapper.selectById(condition.getGroupid());
		 if(amzgroup!=null) {
			 gname=amzgroup.getName();
		 }
		 List<AmazonOrdersReturnVo> list = amzOrderReturnsMapper.selectReturnsList(condition);
		 if(list!=null && list.size()>0) {
			 for(AmazonOrdersReturnVo item:list) {
				 item.setAuthid(auth.getId());
				 
				 item.setGroupname(gname);
				 String orderid = item.getOrderId();
				QueryWrapper<OrdersReport> orderWrapper=new QueryWrapper<OrdersReport>();
				orderWrapper.eq("amazon_order_id", orderid);
				orderWrapper.eq("sku", item.getSku());
				OrdersReport orders = ordersReportMapper.selectOne(orderWrapper);
				 if(orders!=null) {
					 item.setPurchaseDate(orders.getPurchaseDate());
					 item.setItemPrice(orders.getItemPrice());
					 Marketplace market = marketplaceService.findMapByPoint().get(orders.getSalesChannel());
					 if(market!=null) { 
						 item.setMarketname(market.getName());	 
					 }else {
						 item.setMarketname(orders.getSalesChannel());	 
					 }
				 }else if(item.getMarketname()==null) {
					 item.setMarketname(auth.getRegion());
				 }else {
					 Marketplace market = marketplaceService.findMarketplaceByCountry(item.getMarketname());
					 if(market!=null) {
						 item.setMarketname(market.getName());
					 }
				 }
				QueryWrapper<ProductInfo> queryWrapper=new QueryWrapper<ProductInfo>();
				queryWrapper.eq("amazonAuthId", auth.getId());
				queryWrapper.eq("marketplaceid", item.getMarketplaceid());
				queryWrapper.eq("sku", item.getSku());
				ProductInfo info = productInfoMapper.selectOne(queryWrapper);
				if(info!=null) {
					item.setName(info.getName());
					if(info.getImage()!=null) {
						Picture picture = pictureService.getById(info.getImage());
						if(picture.getLocation()!=null) {
							item.setImage(picture.getLocation());
						}else {
							item.setImage(picture.getUrl());
						}
					}
				}
				 
				 
			 }
			//操作Excel
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("sku", "SKU");
			titlemap.put("asin", "ASIN");
			titlemap.put("groupname", "店铺");
			titlemap.put("market", "站点");
			titlemap.put("orderid", "订单号");
			titlemap.put("centerid", "物流中心ID");
			titlemap.put("returndate", "退款日期");
			titlemap.put("dispos", "库存属性");
			titlemap.put("quantity", "数量");
			titlemap.put("reason", "退货原因");
			titlemap.put("custcomment", "退货留言");
			titlemap.put("detailedDispositionName", "库存属性CN");
			titlemap.put("reasonname", "退货原因CN");
			titlemap.put("statusname", "状态CN");
			titlemap.put("purchaseDate", "购买日期");
			titlemap.put("itemPrice", "购买金额");
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
				AmazonOrdersReturnVo item = list.get(i);
				   Cell cell = row.createCell(0); // 在索引0的位置创建单元格(左上端)
					    cell.setCellValue(item.getSku());
 					    cell = row.createCell(1);
 					    cell.setCellValue(item.getAsin());
 					    cell = row.createCell(2);
						cell.setCellValue(item.getGroupname());
			            cell = row.createCell(3);
						cell.setCellValue(item.getMarketname());
		                cell = row.createCell(4);
						cell.setCellValue(item.getOrderId());
	                    cell = row.createCell(5);
						cell.setCellValue(item.getFulfillmentCenterId());
                        cell = row.createCell(6);
						cell.setCellValue(GeneralUtil.formatDate(item.getReturnDate()));
						cell = row.createCell(7);
 						cell.setCellValue(item.getDetailedDisposition());
					    cell = row.createCell(8);
						cell.setCellValue(item.getQuantity());
					    cell = row.createCell(9);
						cell.setCellValue(item.getReason());
					    cell = row.createCell(10);
						cell.setCellValue(item.getCustomerComments());
						cell = row.createCell(11);
						cell.setCellValue(item.getDetailedDispositionName());
						cell = row.createCell(12);
						cell.setCellValue(item.getReasonname());
						cell = row.createCell(13);
						cell.setCellValue(item.getStatusname());
						cell = row.createCell(14);
						cell.setCellValue(GeneralUtil.formatDate(item.getPurchaseDate()));
						cell = row.createCell(15);
						cell.setCellValue(item.getItemPrice()!=null?item.getItemPrice().toString():"");

			} 
			 
		 }
	}

}
