package com.wimoor.amazon.inbound.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import cn.hutool.core.util.StrUtil;
import com.wimoor.amazon.inbound.pojo.dto.ShipTimeDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentItem;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemReceiveDTO;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.orders.mapper.OrdersFulfilledShipmentsFeeMapper;
import com.wimoor.common.user.UserInfo;

import lombok.RequiredArgsConstructor;

@Service("shipInboundItemService")
@RequiredArgsConstructor
public class ShipInboundItemServiceImpl extends  ServiceImpl<ShipInboundItemMapper,ShipInboundItem> implements IShipInboundItemService {
	 
	final IShipInboundTransService shipInboundTransService;
	
	final OrdersFulfilledShipmentsFeeMapper ordersFulfilledShipmentsFeeMapper;
	
	public void saveShipmentsFee(ShipInboundShipment shipment,UserInfo user) {
		//获取分摊费用 存起来
		ShipInboundTrans transfee = shipInboundTransService.lambdaQuery().eq(ShipInboundTrans::getShipmentid, shipment.getShipmentid()).one();
		if(transfee==null||transfee.getSingleprice()==null||transfee.getTransweight()==null) {
			return;
		}
		List<Map<String, Object>> itemlist =this.baseMapper.selectByShipmentid(shipment.getShipmentid());
		BigDecimal totalWeight=new BigDecimal("0");
		BigDecimal totalFee=new BigDecimal("0");
		for(Map<String,Object> item:itemlist) {
			BigDecimal weight =item.get("weight")!=null?new BigDecimal(item.get("weight").toString()):new BigDecimal("0");
			BigDecimal dimweight =item.get("dimweight")!=null?new BigDecimal(item.get("dimweight").toString()):new BigDecimal("0");
			BigDecimal maxWeight=weight.compareTo(dimweight)>0?weight:dimweight;
			if(maxWeight.floatValue()<0.000001){
				throw new BizException(item.get("sku").toString()+"未找到重量，请确认正确维护了本地SKU信息");
			}
			item.put("maxWeight", maxWeight);
			totalWeight=totalWeight.add(maxWeight.multiply(new BigDecimal(item.get("QuantityShipped").toString())));
		}
		if(transfee.getOtherfee()!=null) {
			totalFee=totalFee.add(transfee.getOtherfee());
		}
		if(transfee.getSingleprice()!=null&&transfee.getTransweight()!=null) {
			totalFee=totalFee.add(transfee.getSingleprice().multiply(transfee.getTransweight()));
		}
		int i=0;
		BigDecimal subPrecent=new BigDecimal("1");
		BigDecimal subPrice=new BigDecimal("0");
		for(Map<String,Object> feeitem:itemlist) {
			BigDecimal maxWeight=feeitem.get("maxWeight")!=null?new BigDecimal(feeitem.get("maxWeight").toString()):new BigDecimal("0");
			BigDecimal price=feeitem.get("price")!=null?new BigDecimal(feeitem.get("price").toString()):new BigDecimal("0");
			BigDecimal precent = maxWeight.multiply(new BigDecimal(feeitem.get("QuantityShipped").toString())).divide(totalWeight,6,RoundingMode.FLOOR) ;
			subPrecent=subPrecent.subtract(precent);
			i++;
			if(i==itemlist.size()&&subPrecent.floatValue()>0.000001) {
				precent=precent.add(subPrecent);
			}
			BigDecimal itemfee=  totalFee.multiply(precent).setScale(2,RoundingMode.FLOOR);
			subPrice=subPrice.add(itemfee.subtract(itemfee.setScale(0,RoundingMode.FLOOR)).divide(new BigDecimal("100"),6,RoundingMode.FLOOR));
			if(i==itemlist.size()&&subPrice.floatValue()>0.000001) {
				itemfee=itemfee.add(subPrice);
				itemfee=itemfee.setScale(2,RoundingMode.FLOOR);
			}
			String sellersku= feeitem.get("sellersku")!=null?feeitem.get("sellersku").toString():null;
			ShipInboundItem item = findItem(shipment.getShipmentid(), shipment.getInboundplanid(), sellersku);
			item.setUnittransfee(itemfee.divide(new BigDecimal(item.getQuantity()),2,RoundingMode.FLOOR));
			item.setTotaltransfee(itemfee);
			item.setUnitcost(price.divide(new BigDecimal(item.getQuantity()),2,RoundingMode.FLOOR));
			if(item.getMaterialid()==null&&feeitem.get("materialid")!=null){
				item.setMaterialid(feeitem.get("materialid").toString());
			}
			if(item.getMsku()==null&&feeitem.get("msku")!=null){
				item.setMsku(feeitem.get("msku").toString());
			}
			item.setTotalcost(price);
			this.updateById(item);
		}
	}
	
 
	public Integer refreshOutbound(String shopid,String warehouseid,String msku) {
		return this.baseMapper.refreshOutbound(shopid, warehouseid, msku);
	}
	 public int updateItem(ShipInboundItem item){
				QueryWrapper<ShipInboundItem> queryWrapper = new QueryWrapper<ShipInboundItem>();
				queryWrapper.eq("shipmentid",item.getShipmentid());
				queryWrapper.eq("sellersku",item.getSellersku());
				queryWrapper.eq("inboundplanid",item.getInboundplanid());
	            return this.baseMapper.update(item, queryWrapper);
	        }

		public ShipInboundShipmenSummarytVo summaryShipmentItem(String shipmentid) {
			ShipInboundShipmenSummarytVo itemsum = this.baseMapper.summaryShipmentItem(shipmentid);
			List<ShipInboundItemVo> itemvoList=listByShipmentid(shipmentid);
			itemsum.setItemList(itemvoList);
			return itemsum;
		}
		
		public ShipInboundShipmenSummarytVo summaryShipmentItem(List<String> shipmentids) {
			ShipInboundShipmenSummarytVo itemsum = this.baseMapper.summaryShipmentsItem(shipmentids);
			List<ShipInboundItemVo> itemvoList=listByShipmentsid(shipmentids);
			itemsum.setItemList(itemvoList);
			return itemsum;
		}
		public ShipInboundShipmenSummarytVo summaryShipmentItemWithoutItem(String shipmentid) {
			ShipInboundShipmenSummarytVo itemsum = this.baseMapper.summaryShipmentItem(shipmentid);
			return itemsum;
		}
		
		public List<ShipInboundItemVo> listByShipmentid(String shipmentid){
			List<ShipInboundItemVo> itemvoList=this.baseMapper.selectObjByShipmentid(shipmentid);
			return itemvoList;
		}
		public List<ShipInboundItemVo> listByShipmentsid(List<String> shipmentid){
			List<ShipInboundItemVo> itemvoList=this.baseMapper.selectObjByShipmentsid(shipmentid);
			return itemvoList;
		}
		public ShipInboundItem findItem(String shipmentid, String inboundplanid, String sellersku) {
			// TODO Auto-generated method stub
			QueryWrapper<ShipInboundItem> queryWrapper = new QueryWrapper<ShipInboundItem>();
			queryWrapper.eq("shipmentid",shipmentid);
			queryWrapper.eq("sellersku",sellersku);
			queryWrapper.eq("inboundplanid",inboundplanid);
			return this.baseMapper.selectOne(queryWrapper);
		}


		 
		@Override
		public List<ShipInboundItem> getItemByShipment(String shipmentid) {
			// TODO Auto-generated method stub
			return this.baseMapper.getOneByShipmentid(shipmentid);
		}

		@Override
		public List<Map<String, Object>> getshotTime(String companyid, String groupid, String marketplaceid,
				String sku) {
			// TODO Auto-generated method stub
			return this.baseMapper.getshotTime(companyid, groupid, marketplaceid, sku);
		}
		
		public Integer summaryShipmentSku(String groupid, String marketplaceid, String sku) {
			// TODO Auto-generated method stub
			return this.baseMapper.summaryShipmentSku(groupid, marketplaceid, sku);
		}

		@Override
		public IPage<Map<String, Object>> shipmentReportByLoistics(Page<?>page,Map<String, Object> param) {
			// TODO Auto-generated method stub
			return this.baseMapper.shipmentReportByType(page,param);
		}
		@Override
		public Map<String,List<Map<String,Object>>> shipmentReportByWarhouseCHType(Map<String, Object> param) {
			// TODO Auto-generated method stub
			List<Map<String, Object>> list = this.baseMapper.shipmentReportByWarhouseCHType(param);
			Map<String,List<Map<String,Object>>> result=new HashMap<String,List<Map<String,Object>>>();
			for(Map<String, Object> item:list) {
				String name =item.get("marketname").toString();
				List<Map<String, Object>> resultlist = result.get(name);
				if(resultlist==null) {
					resultlist=new LinkedList<Map<String, Object>>();
				}
				resultlist.add(item);
				result.put(name,resultlist);
			}
			return result;
		}
		
		@Override
		public Map<String, Object> shipmentReportByLoisticsTotal(Map<String, Object> param) {
			// TODO Auto-generated method stub
			return this.baseMapper.shipmentReportByTypeTotal(param);
		}
		
		public void setShipmentReportByLoisticsExcelBook(SXSSFWorkbook workbook, Map<String, Object> param,List<String> groupby) {
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			String type = param.get("type") == null ? "" : param.get("type").toString();
			if (groupby.contains("warehouse")) {
				titlemap.put("warehouse", "FBA仓库");
			}
			if(groupby.contains("channeldetailid")) {
				titlemap.put("logitics", "物流承运商");
				titlemap.put("channame", "物流渠道");
			}
			if(groupby.contains("groupid")){
				titlemap.put("gname", "店铺名称");
			}
			if(groupby.contains("marketplaceid")){
				titlemap.put("market", "站点");
			}
			if(groupby.contains("shipmentid")){
				titlemap.put("shipmentid", "货件号");
			}
			if(groupby.contains("warehouseid")){
				titlemap.put("warehousename", "本地仓库");
			}
			if(groupby.contains("sku")){
				titlemap.put("sku", "sku");
			}
			titlemap.put("totalout", "实际发货数量");
			titlemap.put("totalrec", "实际接收数量");
			titlemap.put("lessrec", "接收差值");
			titlemap.put("worth", "实际发货货值");
			if(!type.equals("sku")) {
				titlemap.put("readweight", "预估运输重量(KG)");
				titlemap.put("transweight_kg", "发货运输重量(KG)");
				titlemap.put("transweight_cbm", "发货运输重量(CBM)");
				titlemap.put("totalbox", "货件箱数");
				titlemap.put("shipfee", "运输费用");
				titlemap.put("totalotherfee", "关税/其他费用");
				titlemap.put("avgtime", "平均物流时效（天）");
				titlemap.put("shipmentnum", "货件票数");
			}
			
			
			titlemap.put("needout", "待发货数量");
			titlemap.put("needrec", "待接收数量");
			if(!type.equals("sku")) {
			    titlemap.put("problem", "异常货件票数");
			}
			List<Map<String, Object>> list = this.baseMapper.shipmentReportByType(param);
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Row row = sheet.createRow(i + 1);
					Map<String, Object> map = list.get(i);
					for (int j = 0; j < titlearray.length; j++) {
						Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
						String key = titlearray[j].toString();
						Object value = map.get(key);
						if (key.equals("shipfee")) {
							if (value != null && value.toString().equals("0E-8")) {
								value = "0";
							}
						}
						if (value != null) {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		}

		@Override
		public IPage<Map<String, Object>> getShipinboundItemBatchCondition(Page<?> page,
				Map<String, Object> param) {
			return this.baseMapper.getShipinboundItemBatchCondition(page,param);
		}
		
		@Override
		public List<Map<String, Object>> findDetailByShipment(String shipmentid,String sku) {
			return  ordersFulfilledShipmentsFeeMapper.findDetailByShipment(shipmentid,sku);
		}

		@Override
		public void downloadBatchListList(SXSSFWorkbook workbook, Map<String, Object> param) {
			 List<Map<String, Object>> list = this.baseMapper.getShipinboundItemBatchCondition(param);
			 if(list!=null && list.size()>0) {
				//操作Excel
				Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
				titlemap.put("groupname", "店铺");
				titlemap.put("marketname", "站点");
				titlemap.put("sku", "平台SKU");
				titlemap.put("name", "产品名称");
				titlemap.put("shipdate", "发货日期");
				titlemap.put("statusname", "状态");
				titlemap.put("number", "单据编码");
				titlemap.put("shipmentid", "货件号");
				titlemap.put("QuantityShipped", "发货数量");
				titlemap.put("QuantityReceived", "接收数量");
				titlemap.put("shipmentstatus", "货件状态");
				titlemap.put("ReceivedDate", "接收日期");
				titlemap.put("QuantityReceivedSub", "扣减数量");
				titlemap.put("unitcost", "单位库存采购成本");
				titlemap.put("unittransfee", "单位头程分摊成本");
				
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
					    Map<String, Object> item = list.get(i);
						if(item==null) {
							continue;
						}
						Row row = sheet.createRow(i + 1);
						for (int j = 0;j < titlearray.length; j++) {
							Cell cell = row.createCell(j); 
							String field=titlearray[j].toString();
							   if(item.get(field)!=null) {
					            	cell.setCellValue(item.get(field).toString());
							   } 
							  
						}
				    } 
				 
			 }
		}

		@Override
		public void updateFeeByShipment(List<ShipInboundItem> list) {
			for (ShipInboundItem item:list) {
				ShipInboundItem inbounditem = this.lambdaQuery().eq(ShipInboundItem::getShipmentid, item.getShipmentid())
				.eq(ShipInboundItem::getSellersku, item.getSellersku()).one();
				if(inbounditem!=null){
					inbounditem.setUnitcost(item.getUnitcost());
					inbounditem.setTotalcost(item.getTotalcost());
					inbounditem.setUnittransfee(item.getUnittransfee());
					inbounditem.setTotaltransfee(item.getTotaltransfee());
					inbounditem.setOperator(item.getOperator());
					inbounditem.setOpttime(item.getOpttime());
					this.updateById(inbounditem);
					ordersFulfilledShipmentsFeeMapper.updateFeeByShipment(item.getShipmentid(),item.getSellersku());
				}
			}
		}

		@Override
		public void updateReceiveDate(ShipInboundItemReceiveDTO dto,UserInfo user) {
			Date date = dto.getDate();
			Integer days=dto.getDay();
			for (int i = 0; i < dto.getItemlist().size(); i++) {
				Map<String, Object> item = dto.getItemlist().get(i);
				LambdaQueryWrapper<ShipInboundItem> queryWrapper=new LambdaQueryWrapper<ShipInboundItem>();
				queryWrapper.eq(ShipInboundItem::getShipmentid, item.get("shipmentid"));
				queryWrapper.eq(ShipInboundItem::getSellersku, item.get("sku"));
				ShipInboundItem nowitem = this.baseMapper.selectOne(queryWrapper);
				if(nowitem==null) {continue;}
				nowitem.setOperator(user.getId());
				nowitem.setOpttime(new Date());
				if(date!=null) {
					    nowitem.setReceivedDate(date);
						this.updateById(nowitem);
				}else {
					if(nowitem.getReceivedDate()!=null) {
						Calendar cal=Calendar.getInstance(); 
						cal.setTime(nowitem.getReceivedDate()); 
						cal.add(Calendar.DATE, days);
						nowitem.setReceivedDate(cal.getTime());
						this.updateById(nowitem);
					}
				}
			}
		}

	@Override
	public Map<String, Object> getLastShipmentQty( Map<String,Object> param) {
		param.put("shipmentidlist",null);
		if (param.get("shipmentids")!=null&&StrUtil.isNotBlank(param.get("shipmentids").toString())) {
			String shipmentids=param.get("shipmentids").toString();
			 List<String> shipmentidlist= Arrays.asList(shipmentids.split(","));
			 if(shipmentidlist.size()>0){
				 param.put("shipmentidlist",shipmentidlist);
			 }
		}
		param.put("mskulist",null);
		if (param.get("msku")!=null&&StrUtil.isNotBlank(param.get("msku").toString())) {
			String msku=param.get("msku").toString();
			List<String> mskulist= Arrays.asList(msku.split(","));
			if(mskulist.size()>0){
				param.put("mskulist",mskulist);
			}
		}
		return this.baseMapper.getLastShipmentQty(param);
	}
	@Override
	public IPage<Map<String, Object>> getShipTimeList(ShipTimeDTO dto) {
		return this.baseMapper.getShipTimeList(dto.getPage(),dto);
	}

	@Override
	public void downloadTimeList(SXSSFWorkbook workbook, ShipTimeDTO dto) {
		List<Map<String, Object>> list = this.baseMapper.getShipTimeList(dto);
		Map<Integer, String> title = new HashMap<Integer, String>();
		title.put(0, "平台SKU");
		title.put(1, "本地SKU");
		title.put(2, "产品名称");
		title.put(3, "仓库名称");
		title.put(4, "货件");
		title.put(5, "店铺");
		title.put(6, "站点");
		title.put(7, "发货数量");
		title.put(8, "计划时间");
		title.put(9, "审核时间");
		title.put(10, "配货时间");
		title.put(11, "装箱时间");
		title.put(12, "出库时间");
		title.put(13, "收货时间");
		title.put(14, "完成时间");
		title.put(15, "发货时效");
		title.put(16, "收货时效");
		title.put(17, "总时效");

		Map<String, String> titlemap = new HashMap<String, String>();
		titlemap.put("平台SKU","sku");
		titlemap.put("本地SKU","msku");
		titlemap.put("产品名称","name");
		titlemap.put("仓库名称","wname");
		titlemap.put("店铺","gname");
		titlemap.put("站点","marketname");
		titlemap.put("货件","shipmentid");
		titlemap.put("发货数量","qty");
		titlemap.put("计划时间","createdate");
		titlemap.put("审核时间","approveDate");
		titlemap.put("配货时间","shipDate");
		titlemap.put("装箱时间","boxDate");
		titlemap.put("出库时间","shippedDate");
		titlemap.put("收货时间","receiveDate");
		titlemap.put("完成时间","endDate");
		titlemap.put("发货时效","shipdays");
		titlemap.put("收货时效","receivedays");
		titlemap.put("总时效","days");

		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		for(Integer key: title.keySet()){
			Cell cell = row.createCell(key);
			cell.setCellValue(title.get(key));
		}
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> rmap = list.get(i);
			Row crow = sheet.createRow(i+1);
			for(Integer key: title.keySet()){
				Cell cell = crow.createCell(key);
				if(rmap.get(titlemap.get(title.get(key)))==null){
					cell.setCellValue("");
				} else {
					cell.setCellValue(rmap.get(titlemap.get(title.get(key))).toString());
				}
			}
		}


	}
}
