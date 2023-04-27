package com.wimoor.amazon.inbound.service.impl;

import java.util.LinkedHashMap;
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
import com.wimoor.amazon.inbound.service.IShipInboundItemService;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundItemVo;
import com.wimoor.api.amzon.inbound.pojo.vo.ShipInboundShipmenSummarytVo;

import lombok.RequiredArgsConstructor;

@Service("shipInboundItemService")
@RequiredArgsConstructor
public class ShipInboundItemServiceImpl extends  ServiceImpl<ShipInboundItemMapper,ShipInboundItem> implements IShipInboundItemService {
	 

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
}
