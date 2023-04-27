package com.wimoor.amazon.orders.service.impl;

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
		 IPage<AmazonOrdersReturnVo> list = amzOrderReturnsMapper.selectReturnsList(condition.getPage(),condition);
		 if(list.getRecords()!=null && list.getRecords().size()>0) {
			 for(AmazonOrdersReturnVo item:list.getRecords()) {
				 item.setAuthid(auth.getId());
				 item.setGroupname(gname);
				 OrdersReport orders = ordersReportMapper.selectBySkuOrder(item.getSku(),item.getOrderId());
				 Marketplace market = null;
				 if(orders!=null) {
					 item.setPurchaseDate(orders.getPurchaseDate());
					 item.setItemPrice(orders.getItemPrice());
					 market = marketplaceService.findMapByPoint().get(orders.getSalesChannel());
					 if(market!=null) { 
						 item.setMarketname(market.getName());	 
					 }else {
						 item.setMarketname(orders.getSalesChannel());	 
					 }
				 }else if(item.getMarketname()==null) {
					 item.setMarketname(auth.getRegion());
				 }else if(market==null) {
					  market = marketplaceService.findMarketplaceByCountry(item.getMarketname());
					 if(market!=null) {
						 item.setMarketname(market.getName());
					 }
				 }
				QueryWrapper<ProductInfo> queryWrapper=new QueryWrapper<ProductInfo>();
				queryWrapper.eq("amazonAuthId", auth.getId());
				queryWrapper.eq("marketplaceid", market.getMarketplaceid());
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
		 }
		 return list;
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
						cell.setCellValue(item.getReturnDate());
						cell = row.createCell(7);
 						cell.setCellValue(item.getDetailedDisposition());
					    cell = row.createCell(8);
						cell.setCellValue(item.getQuantity());
					    cell = row.createCell(9);
						cell.setCellValue(item.getReason());
					    cell = row.createCell(10);
						cell.setCellValue(item.getCustomerComments());
			} 
			 
		 }
	}

}
