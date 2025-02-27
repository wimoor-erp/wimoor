package com.wimoor.amazon.inboundV2.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResponse;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResult;
import com.amazon.spapi.model.fulfillmentinbound.InboundShipmentItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentItemV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentItem;
import com.wimoor.amazon.inboundV2.service.IShipInboundPlanService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentService;
import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.result.Result;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.pojo.dto.ShipInboundItemReceiveDTO;
import com.wimoor.amazon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundItemV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.vo.ShipInboundItemVo;
import com.wimoor.amazon.inboundV2.service.IShipInboundItemService;
import com.wimoor.amazon.orders.mapper.OrdersFulfilledShipmentsFeeMapper;
import com.wimoor.common.user.UserInfo;

import lombok.RequiredArgsConstructor;

@Service("shipInboundItemV2Service")
@RequiredArgsConstructor
public class ShipInboundItemServiceImpl extends  ServiceImpl<ShipInboundItemV2Mapper,ShipInboundItem> implements IShipInboundItemService {
	 
	final IShipInboundTransService shipInboundTransService;
	final IShipInboundShipmentService shipInboundShipmentService;
	final IShipInboundPlanService shipInboundPlanService;
	final ShipInboundShipmentItemV2Mapper shipInboundShipmentItemV2Mapper;
	final IProductInfoService iProductInfoService;
	final OrdersFulfilledShipmentsFeeMapper ordersFulfilledShipmentsFeeMapper;
	final ErpClientOneFeignManager	erpClientOneFeignManager;

	public void saveShipmentsFee(ShipInboundShipment shipment,UserInfo user) {
		 
	}
	
 
	public Integer refreshOutbound(String shopid,String warehouseid,String msku) {
		return this.baseMapper.refreshOutbound(shopid, warehouseid, msku);
	}
	 public int updateItem(ShipInboundItem item){
				QueryWrapper<ShipInboundItem> queryWrapper = new QueryWrapper<ShipInboundItem>();
				 
	            return this.baseMapper.update(item, queryWrapper);
	        }


		public ShipInboundShipmenSummarytVo summaryShipmentItemWithoutItem(String shipmentid) {
			ShipInboundShipmenSummarytVo itemsum = this.baseMapper.summaryShipmentItem(shipmentid);
			return itemsum;
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
	public void setShipmentReportByLoisticsExcelBook(SXSSFWorkbook workbook, Map<String, Object> param, List<String> groupby) {
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
		
		public void setShipmentReportByLoisticsExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			String type = param.get("type") == null ? "" : param.get("type").toString();
			if (type.equals("warehouse")) {
				titlemap.put("warehouse", "FBA仓库");
			}else if(type.equals("logitics")) {
				titlemap.put("logitics", "物流承运商");
				titlemap.put("channame", "物流渠道");
			}else {
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
		 
		}

		@Override
		public void updateReceiveDate(ShipInboundItemReceiveDTO dto,UserInfo user) {
			 
		}


		@Override
		public ShipInboundShipmenSummarytVo summaryShipmentItem(String shipmentid) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public ShipInboundShipmenSummarytVo summaryShipmentItem(List<String> shipmentids) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<ShipInboundItemVo> listByFormid(String formid) {
			// TODO Auto-generated method stub
			List<ShipInboundItemVo> itemvoList=this.baseMapper.selectObjByFormid(formid);


			 List<String> skulist=itemvoList.stream().map(itemvo->itemvo.getMsku()).collect(Collectors.toList());
			 PlanDTO dto = new PlanDTO();
			 dto.setMskulist(skulist);
			 Result<List<Map<String, Object>>> listResult = erpClientOneFeignManager.getMskuInventory(dto);
			 Map<String,Map<String,Object>> skuInfo=listResult.getData().stream().collect(Collectors.toMap(item->item.get("sku").toString(), item->item));
			 for(ShipInboundItemVo itemvo:itemvoList) {
				 Map<String, Object> mskuinfo = skuInfo.get(itemvo.getMsku());
				 if(mskuinfo!=null) {
					 if(mskuinfo.get("image")!=null){
						 itemvo.setImage(mskuinfo.get("image").toString());
					 }
				 }
			 }

			return itemvoList;
		}

	@Override
	public void handlerItemResult(AmazonAuthority auth, Marketplace market, GetShipmentItemsResponse result, String shipmentid, boolean needshipqty) {
		auth.setUseApi("getShipmentItems");
		GetShipmentItemsResult itemResult = result.getPayload();
		if(itemResult.getNextToken()!=null) {
			needshipqty=false;
		}
		handlerItemResultData(auth,itemResult,shipmentid,needshipqty);
	}

	private List<ShipInboundShipmentItem> handlerItemResultData(AmazonAuthority auth, GetShipmentItemsResult itemResult, String confirmShipmentid, boolean needshipqty) {
		Boolean hasReceived=false;
		Boolean errorReceived=false;
		String shipmentid="";
		List<ShipInboundShipmentItem> myResult=new ArrayList<ShipInboundShipmentItem>();
		Set<String> hasSku=new HashSet<String>();
		ShipInboundShipment shipment=shipInboundShipmentService.getOne(
				new LambdaQueryWrapper<ShipInboundShipment>()
						.eq(ShipInboundShipment::getShipmentConfirmationId, confirmShipmentid)
		);
		shipmentid= shipment.getShipmentid();
		for (InboundShipmentItem item:itemResult.getItemData()) {
			LambdaQueryWrapper<ShipInboundShipmentItem> query = new LambdaQueryWrapper<ShipInboundShipmentItem>()
					.eq(ShipInboundShipmentItem::getShipmentid, shipment.getShipmentid())
					.eq(ShipInboundShipmentItem::getSku, item.getSellerSKU());
			ShipInboundShipmentItem inbounditem = shipInboundShipmentItemV2Mapper.selectOne(query);
			hasSku.add(item.getSellerSKU());
			if(inbounditem!=null) {
				inbounditem.setReceived(item.getQuantityReceived());
				if (item.getQuantityReceived() != null && item.getQuantityReceived() > 0) {
					hasReceived = true;
				}
				int rec=0;
				if(item.getQuantityReceived()!=null) {
					rec=item.getQuantityReceived();
				}
				int shipped=item.getQuantityShipped();
				int amount = rec-shipped;

				if(errorReceived==true) {
					if (amount>=10 || amount<=-10) {
						errorReceived = false;
					}
				}
				inbounditem.setQuantity(shipped);
				inbounditem.setShipmentid(shipmentid);
				shipInboundShipmentItemV2Mapper.updateById(inbounditem);
				myResult.add(inbounditem);
			}
			else if(inbounditem==null)  {
				inbounditem=new ShipInboundShipmentItem();
				inbounditem.setShipmentid(shipmentid);
				inbounditem.setSku(item.getSellerSKU());
				inbounditem.setQuantity(item.getQuantityShipped());
				inbounditem.setQuantity(item.getQuantityShipped());
				inbounditem.setReceived(item.getQuantityReceived());
				if (item.getQuantityReceived() != null && item.getQuantityReceived() > 0) {
					hasReceived = true;
				}
				int rec=0;
				if(item.getQuantityReceived()!=null) {
					rec=item.getQuantityReceived();
				}
				int shipped=item.getQuantityShipped();
				int amount = rec-shipped;

				if(errorReceived==true) {
					if (amount>=10 || amount<=-10) {
						errorReceived = false;
					}
				}
				ShipInboundPlan plan = shipInboundPlanService.getById(shipment.getFormid());
				String msku=iProductInfoService.getMSKU(auth.getId(), plan.getMarketplaceid(), item.getSellerSKU());
				inbounditem.setMsku(msku);
				inbounditem.setShipmentid(shipmentid);
				shipInboundShipmentItemV2Mapper.insert(inbounditem);
				myResult.add(inbounditem);
			}

		}
		if (StrUtil.isNotEmpty(shipmentid)) {
			if (shipment!=null&&shipment.getStart_receive_date() == null && hasReceived) {
				shipment.setStart_receive_date(new Date());
				shipInboundShipmentService.updateById(shipment);
			}
			if(needshipqty) {
				LambdaQueryWrapper<ShipInboundShipmentItem> queryShipmentItem = new LambdaQueryWrapper<ShipInboundShipmentItem>()
						.eq(ShipInboundShipmentItem::getShipmentid, shipmentid);
				List<ShipInboundShipmentItem> inbounditemList = shipInboundShipmentItemV2Mapper.selectList(queryShipmentItem);
				for(ShipInboundShipmentItem item:inbounditemList) {
					if(hasSku.contains(item.getSku())) {
						continue;
					}else {
						LambdaQueryWrapper<ShipInboundShipmentItem> query = new LambdaQueryWrapper<ShipInboundShipmentItem>()
								.eq(ShipInboundShipmentItem::getShipmentid, item.getShipmentid())
								.eq(ShipInboundShipmentItem::getSku, item.getSku());
						item.setQuantity(0);
						shipInboundShipmentItemV2Mapper.update(item, query);
					}
				}
			}
		}
		return myResult;
	}


}
