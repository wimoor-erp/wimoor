package com.wimoor.amazon.orders.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.mapper.AmazonGroupMapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.orders.mapper.AmzOrderRemovesMapper;
import com.wimoor.amazon.orders.mapper.AmzOrderReturnsMapper;
import com.wimoor.amazon.orders.mapper.OrdersReportMapper;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersRemoveDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderRemoves;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;
import com.wimoor.amazon.orders.service.IAmzOrderRemovesService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.common.service.IPictureService;

@Service
public class AmzOrderRemovesServiceImpl extends ServiceImpl<AmzOrderRemovesMapper,AmzOrderRemoves> implements IAmzOrderRemovesService{
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
	public IPage<AmazonOrdersRemoveVo> selectRemovesList(AmazonOrdersRemoveDTO dto) {
		IPage<AmazonOrdersRemoveVo> list = this.baseMapper.selectRemoveList(dto.getPage(),dto);
		if(list!=null && list.getRecords()!=null && list.getRecords().size()>0) {
			for(int i=0;i<list.getRecords().size();i++) {
				AmazonOrdersRemoveVo item = list.getRecords().get(i);
				Map<String, Object> paramMap2=new HashMap<String, Object>();
				paramMap2.put("authid", item.getAmazonAuthId());
				paramMap2.put("sku", item.getSku());
				Map<String, Object> maps= this.baseMapper.selectPInfoBySku(paramMap2);
				if(maps!=null) {
					if(maps.get("image")!=null)item.setImage(maps.get("image").toString());
					if(maps.get("name")!=null)item.setName(maps.get("name").toString());
					if(maps.get("asin")!=null)item.setAsin(maps.get("asin").toString());
				}
			}
		}else {
			return list;
		}
		return list;
	}
	
	@Override
	public void downloadRemoveslist(SXSSFWorkbook workbook,AmazonOrdersRemoveDTO condition) {
		AmazonAuthority auth = amazonAuthorityService.getById(condition.getAmazonauthid());
		 if(auth!=null) {
			 condition.setSellerid(auth.getSellerid());
		 }
		 String gname="";
		 AmazonGroup amzgroup = amazonGroupMapper.selectById(condition.getGroupid());
		 if(amzgroup!=null) {
			 gname=amzgroup.getName();
		 }
		 List<AmazonOrdersRemoveVo> list = this.baseMapper.selectRemoveList(condition);
		 if(list!=null && list.size()>0) {
			 for(AmazonOrdersRemoveVo item:list) { 
					Map<String, Object> paramMap2=new HashMap<String, Object>();
					paramMap2.put("authid", item.getAmazonAuthId());
					paramMap2.put("sku", item.getSku());
					Map<String, Object> maps= this.baseMapper.selectPInfoBySku(paramMap2);
					if(maps!=null) {
						if(maps.get("image")!=null)item.setImage(maps.get("image").toString());
						if(maps.get("name")!=null)item.setName(maps.get("name").toString());
						if(maps.get("asin")!=null)item.setAsin(maps.get("asin").toString());
					}
			 }
			//操作Excel
			List<String> titlemap = new LinkedList<String>();
			titlemap.add("SKU");
			titlemap.add("名称");
			titlemap.add( "ASIN");
			titlemap.add("店铺");
			titlemap.add("订单类型");
			titlemap.add("订单号");
			titlemap.add("处理方式");
			titlemap.add("订单状态");
			titlemap.add("请求数量");
			titlemap.add( "取消数量");
			titlemap.add("处理中数量");
			titlemap.add("已处理数量");
			titlemap.add("发货数量");
			titlemap.add("移除费");
			titlemap.add("币种");
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			 
			for (int i = 0; i < titlemap.size(); i++) {
				Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				cell.setCellValue(titlemap.get(i).toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				AmazonOrdersRemoveVo item = list.get(i);
				  int cellnum=0;
				   Cell cell = row.createCell(cellnum++); // 在索引0的位置创建单元格(左上端)
					    cell.setCellValue(item.getSku());
					    cell = row.createCell(cellnum++);
 					    cell.setCellValue(item.getName());
 					    cell = row.createCell(cellnum++);
 					    cell.setCellValue(item.getAsin());
 					    cell = row.createCell(cellnum++);
						cell.setCellValue(gname);
			            cell = row.createCell(cellnum++);
						cell.setCellValue(item.getOrderType());
		                cell = row.createCell(cellnum++);
						cell.setCellValue(item.getOrderId());
	                    cell = row.createCell(cellnum++);
						cell.setCellValue(item.getDisposition());
                        cell = row.createCell(cellnum++);
						cell.setCellValue(item.getOrderStatus());
						cell = row.createCell(cellnum++);
 						cell.setCellValue(item.getRequestedQuantity());
					    cell = row.createCell(cellnum++);
						cell.setCellValue(item.getCancelledQuantity()==null?0:item.getCancelledQuantity());
					    cell = row.createCell(cellnum++);
						cell.setCellValue(item.getInProcessQuantity()==null?0:item.getInProcessQuantity());
					    cell = row.createCell(cellnum++);
						cell.setCellValue(item.getDisposedQuantity()==null?0:item.getDisposedQuantity());
						cell = row.createCell(cellnum++);
						cell.setCellValue(item.getShippedQuantity()==null?0:item.getShippedQuantity());
						cell = row.createCell(cellnum++);
						cell.setCellValue(item.getRemovalFee()!=null?item.getRemovalFee().toString():"");
						cell = row.createCell(cellnum++);
						cell.setCellValue(item.getCurrency()==null?"":item.getCurrency());
			} 
			 
		 }
	}

}
