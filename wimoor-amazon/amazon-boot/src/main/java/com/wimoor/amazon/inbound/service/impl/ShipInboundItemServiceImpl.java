package com.wimoor.amazon.inbound.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundItemMapper;
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
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("shopid", user.getCompanyid());
		param.put("shipmentid", shipment.getShipmentid());
		List<Map<String, Object>> feelist = shipInboundTransService.calculateTransFeeSharedDetail(param);
		if(feelist!=null && feelist.size()>0) {
			for (int i = 0; i < feelist.size(); i++) {
				Map<String, Object> feeitem = feelist.get(i);
				Object skufeeavg = feeitem.get("skufeeavg");
				Object skufee = feeitem.get("skufee");
				Object msku=feeitem.get("msku");
				String psku=feeitem.get("sellersku").toString();
				ShipInboundItem item = findItem(shipment.getShipmentid(), shipment.getInboundplanid(), psku);
				if(item!=null) {
					if(msku!=null&&item.getMsku()==null) {
						item.setMsku(msku.toString());
					}
					if(skufeeavg!=null) {
						item.setUnittransfee(new BigDecimal(skufeeavg.toString()));
					}else if(skufee!=null) {
						BigDecimal skufees = new BigDecimal(skufee.toString());
						if(skufees.compareTo(BigDecimal.ZERO)==0) {
							item.setUnittransfee(new BigDecimal(0));
						}else {
							BigDecimal fee=new BigDecimal(skufee.toString()).divide(new BigDecimal(item.getQuantityshipped()),2,RoundingMode.HALF_UP);
							item.setUnittransfee(fee);
						}
						
					}
					if(skufee!=null) {
						item.setTotaltransfee(new BigDecimal(skufee.toString()));
					}
					if(skufee!=null || skufeeavg!=null) {
						item.setOperator(user.getId());
						item.setOpttime(new Date());
						this.updateById(item);
					}
				}
			}
		}
	}
	
	public void saveCostFee(ShipInboundShipment shipment,UserInfo user) {
		//获取item的quantityshipped 然后乘以unitcost 存起来
		List<ShipInboundItem> itemlist = this.getItemByShipment(shipment.getShipmentid());
		if(itemlist!=null && itemlist.size()>0) {
			for (int i = 0; i < itemlist.size(); i++) {
				ShipInboundItem item = itemlist.get(i);
				if(item!=null) {
					if(item.getUnitcost()!=null) {
						item.setTotalcost(item.getUnitcost().multiply(new BigDecimal(item.getQuantityshipped())));
						this.updateById(item);
					}
				}
			}
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
		
		public ShipInboundShipmenSummarytVo summaryShipmentItemWithoutItem(String shipmentid) {
			ShipInboundShipmenSummarytVo itemsum = this.baseMapper.summaryShipmentItem(shipmentid);
			return itemsum;
		}
		
		public List<ShipInboundItemVo> listByShipmentid(String shipmentid){
			List<ShipInboundItemVo> itemvoList=this.baseMapper.selectObjByShipmentid(shipmentid);
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
			for (ShipInboundItem item:list) {
				ShipInboundItem inbounditem = this.lambdaQuery().eq(ShipInboundItem::getShipmentid, item.getShipmentid())
				.eq(ShipInboundItem::getSellersku, item.getSellersku()).one();
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
