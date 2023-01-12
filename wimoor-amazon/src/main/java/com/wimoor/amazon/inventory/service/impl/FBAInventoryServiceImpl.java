package com.wimoor.amazon.inventory.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeign;
import com.wimoor.amazon.inventory.mapper.AmzInventoryPlanningMapper;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;
import com.wimoor.amazon.inventory.service.IFBAInventoryService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FBAInventoryServiceImpl extends ServiceImpl<InventoryReportMapper,InventoryReport> implements IFBAInventoryService{
	
	final IProductInfoService iProductInfoService;
	
	final AmzInventoryPlanningMapper amzInventoryPlanningMapper;
	
	final ErpClientOneFeign erpClientOneFeign;
	
	public List<Map<String, Object>> findByTypeWithStockCycle(String msku, String shopid) {
		List<Map<String, Object>> list =this.baseMapper.findFBAWithStockCycle(msku, shopid);
		return list;
	}	
	public Map<String, Object> findSumByType(Map<String,Object> param) {
		return this.baseMapper.findFBASum(param);
	}
	
	public Map<String, Object> findFBAInvDetailById(String sku, String warehouseid, String shopid,String groupid) {
		return this.baseMapper.findFBAInvDetailById(sku, warehouseid, shopid,groupid);
	}
	
	public List<Map<String, String>> getInvDaySumField(Map<String, Date> parameter) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Calendar calendar = Calendar.getInstance();
		Date endDate = parameter.get("endDate");
		Date beginDate = parameter.get("beginDate");
		calendar.setTime(beginDate);
		Date end = endDate;
		for (Date step = calendar.getTime(); step.before(end) || step.equals(end); calendar.add(Calendar.DATE, 1), step = calendar.getTime()) {
			String field = GeneralUtil.formatDate(step, GeneralUtil.FMT_YMD);
			Map<String, String> map = new HashMap<String, String>();
			map.put("byday", field);
			map.put("field", "v" + field);
			list.add(map);
		}
		return list;
	}

	public IPage<Map<String, Object>> getFBAInvDayDetail(Page<?> page,Map<String, Object> parameter) {
		Map<String, Date> pmap = new HashMap<String, Date>();
		String endDateStr = (String) parameter.get("endDate");
		String beginDateStr = (String) parameter.get("beginDate");
		Date endDate = null;
		Date beginDate = null;
		if (endDateStr != null && beginDateStr != null) {
			endDate = GeneralUtil.getDatez(endDateStr);
			beginDate = GeneralUtil.getDatez(beginDateStr);
		}
		pmap.put("beginDate", beginDate);
		pmap.put("endDate", endDate);
		List<Map<String, String>> fieldlist = getInvDaySumField(pmap);
		parameter.put("fieldlist", fieldlist);

		IPage<Map<String, Object>> pagelist = this.baseMapper.getFBAInvDayDetail(page,parameter);
//		Map<String, Object> map = inventoryReportMapper.getFBAInvDayTotal(parameter);
		if (pagelist != null && pagelist.getTotal() > 0) {
			String marketplaceid = null;
			String groupid = null;
			if (parameter.get("warehouse") != null) {
				marketplaceid = parameter.get("warehouse").toString();
			}
			if (parameter.get("groupid") != null) {
				groupid = parameter.get("groupid").toString();
			}
			for (Map<String, Object> pagemap : pagelist.getRecords()) {
				String sku_p = pagemap.get("sku").toString();
				Map<String, Object> product = iProductInfoService.findNameAndPicture(sku_p, marketplaceid, groupid);
				if (product != null) {
					String image_location =null;
					if ( product.get("image") != null) {
						image_location = product.get("image").toString();
					}else {
						image_location = "images/systempicture/noimage40.png";
					}
					pagemap.put("pname", product.get("name"));
				}
			}
//			pagelist.get(0).putAll(map);
		}
		return pagelist;
	}
	
	public IPage<AmzInventoryPlanningVo> selectPlanPageList(InventoryPlanningDTO dto){
		IPage<AmzInventoryPlanningVo> pagelist = amzInventoryPlanningMapper.selectPageList(dto.getPage(), dto);
		if(pagelist!=null && pagelist.getRecords()!=null && pagelist.getRecords().size()>0) {
			List<AmzInventoryPlanningVo> list = pagelist.getRecords();
			for (int i = 0; i < list.size(); i++) {
				AmzInventoryPlanningVo item = list.get(i);
				Result<Map<String, Object>> res = erpClientOneFeign.getMaterialBySKUAction(item.getMsku(),dto.getShopid());
				Map<String, Object> result = res.getData();
				if(result!=null && result.get("price")!=null) {
					String purchasecost = result.get("price").toString();
					BigDecimal cost=new BigDecimal(purchasecost);
					item.setPurchasecost(cost);
				}
				if(result!=null && result.get("name")!=null) {
					String name = result.get("name").toString();
					item.setPname(name);
				}
			}
		}
		return pagelist;
	}
	String getValueToString(Integer value) {
		if(value==null)return "0";
		else return value.toString();
	}
	private String getValueToString(BigDecimal value) {
		// TODO Auto-generated method stub
		if(value==null)return "0";
		else return value.toString();
	}
	@Override
	public void downloadPlanList(SXSSFWorkbook workbook, InventoryPlanningDTO dto) {
		 List<AmzInventoryPlanningVo> list = amzInventoryPlanningMapper.selectPageList(dto);
		 if(list!=null && list.size()>0) {
			 for (int i = 0; i < list.size(); i++) {
					AmzInventoryPlanningVo item = list.get(i);
					Result<Map<String, Object>> res = erpClientOneFeign.getMaterialBySKUAction(item.getMsku(),dto.getShopid());
					Map<String, Object> result = res.getData();
					if(result!=null && result.get("price")!=null) {
						String purchasecost = result.get("price").toString();
						BigDecimal cost=new BigDecimal(purchasecost);
						item.setPurchasecost(cost);
					}
			}
			//操作Excel
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("sku", "SKU");
			titlemap.put("skuname", "产品名称");
			titlemap.put("owner", "产品负责人");
			titlemap.put("gname", "店铺");
			titlemap.put("market", "站点");
			titlemap.put("purchasecost", "采购成本");
			titlemap.put("snapshotDate", "更新时间");
			titlemap.put("available", "可用库存");
			titlemap.put("inboundQuantity", "待入库总数");
			titlemap.put("invAge0To30Days", "库龄数量30天内");
			titlemap.put("invAge0To90Days", "库龄数量90天内");
			titlemap.put("invAge31To60Days", "库龄数量31-60天");
			titlemap.put("invAge61To90Days", "库龄数量61-90天");
			titlemap.put("invAge91To180Days", "库龄数量91-180天");
			titlemap.put("invAge181To270Days", "库龄数量181-270天");
			titlemap.put("invAge271To365Days", "库龄数量271-365天");
			titlemap.put("invAge181To330Days", "库龄数量181-330天");
			titlemap.put("invAge331To365Days", "库龄数量331-365天");
			titlemap.put("invAge365PlusDays", "库龄数量365天及以上");
			titlemap.put("unitsShippedT7", "最近7天发货");
			titlemap.put("unitsShippedT30", "最近30天发货");
			titlemap.put("unitsShippedT60", "最近60天发货");
			titlemap.put("unitsShippedT90", "最近90天发货");
			titlemap.put("salesShippedLast7Days", "最近7天销售额(站点币种)");
			titlemap.put("salesShippedLast30Days", "最近30天销售额(站点币种)");
			titlemap.put("salesShippedLast60Days", "最近60天销售额(站点币种)");
			titlemap.put("salesShippedLast90Days", "最近90天销售额(站点币种)");
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
				    AmzInventoryPlanningVo item = list.get(i);
					if(item==null) {
						continue;
					}
						Row row = sheet.createRow(i + 1);
						Cell cell = row.createCell(0); // 在索引0的位置创建单元格(左上端)
					    cell.setCellValue(item.getSku());
					    cell = row.createCell(1);
						cell.setCellValue(item.getSkuname());
						cell = row.createCell(2);
						cell.setCellValue(item.getOwner());
 					    cell = row.createCell(3);
 					    cell.setCellValue(item.getGname());
 					    cell = row.createCell(4);
						cell.setCellValue(item.getMarket());
			            cell = row.createCell(5);
			            if(item.getPurchasecost()!=null) {
			            	cell.setCellValue(item.getPurchasecost().toString());
						}else {
							cell.setCellValue("");
						}
		                cell = row.createCell(6);
						cell.setCellValue(GeneralUtil.formatDate(item.getSnapshotDate()));
	                    cell = row.createCell(7);
	                    if(item.getAvailable()!=null) {
	                    	cell.setCellValue(item.getAvailable());
	                    }else {
	                    	cell.setCellValue("");
	                    }
                        cell = row.createCell(8);
						cell.setCellValue(getValueToString(item.getInboundQuantity()));
						cell = row.createCell(9);
 						cell.setCellValue(getValueToString(item.getInvAge0To30Days()));
					    cell = row.createCell(10);
						cell.setCellValue(getValueToString(item.getInvAge0To90Days()));
					    cell = row.createCell(11);
						cell.setCellValue(getValueToString(item.getInvAge31To60Days()));
					    cell = row.createCell(12);
						cell.setCellValue(getValueToString(item.getInvAge61To90Days()));
						cell = row.createCell(13);
						cell.setCellValue(getValueToString(item.getInvAge91To180Days()));
						cell = row.createCell(14);
						cell.setCellValue(getValueToString(item.getInvAge181To270Days()));
						cell = row.createCell(15);
						cell.setCellValue(getValueToString(item.getInvAge271To365Days()));
						cell = row.createCell(16);
						cell.setCellValue(getValueToString(item.getInvAge181To330Days()));
						cell = row.createCell(17);
						cell.setCellValue(item.getInvAge331To365Days());
						cell = row.createCell(18);
						cell.setCellValue(getValueToString(item.getInvAge365PlusDays()));
						cell = row.createCell(19);
						if(item.getUnitsShippedT7()!=null) {
							cell.setCellValue(getValueToString(item.getUnitsShippedT7()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(20);
						if(item.getUnitsShippedT30()!=null) {
							cell.setCellValue(getValueToString(item.getUnitsShippedT30()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(21);
						if(item.getUnitsShippedT60()!=null) {
							cell.setCellValue(getValueToString(item.getUnitsShippedT60()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(22);
						if(item.getUnitsShippedT90()!=null) {
							cell.setCellValue(getValueToString(item.getUnitsShippedT90()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(23);
						if(item.getSalesShippedLast7Days()!=null) {
							cell.setCellValue(getValueToString(item.getSalesShippedLast7Days()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(24);
						if(item.getSalesShippedLast30Days()!=null) {
							cell.setCellValue(getValueToString(item.getSalesShippedLast30Days()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(25);
						if(item.getSalesShippedLast60Days()!=null) {
							cell.setCellValue(getValueToString(item.getSalesShippedLast60Days()));
						}else {
							cell.setCellValue("");
						}
						cell = row.createCell(26);
						if(item.getSalesShippedLast90Days()!=null) {
							cell.setCellValue(getValueToString(item.getSalesShippedLast90Days()));
						}else {
							cell.setCellValue("");
						}
						
			} 
			 
		 }
	}

	@Override
	public AmzInventoryPlanningVo summaryPlanning(InventoryPlanningDTO dto) {
		List<AmzInventoryPlanningVo> list = amzInventoryPlanningMapper.selectPageList(dto);
		if(list!=null && list.size()>0) {
			AmzInventoryPlanningVo summary=new AmzInventoryPlanningVo();
			int age0To30Days=0;
			int age0To90Days=0;
			int age181To270Days=0;
			int age181To330Days=0;
			int age271To365Days=0;
			int age31To60Days=0;
			int age331To365Days=0;
			int age365PlusDays=0;
			int age61To90Days=0;
			int age91To180Days=0;
			int estimatedExcessQuantity=0;
			int unitsShippedT7=0;
			int unitsShippedT30=0;
			int unitsShippedT60=0;
			int unitsShippedT90=0;
			BigDecimal longStroageFee=new BigDecimal(0);
			BigDecimal longStroageMonthFee=new BigDecimal(0);
			for (int i = 0; i < list.size(); i++) {
				AmzInventoryPlanningVo item = list.get(i);
				age0To30Days+=item.getInvAge0To30Days();
				age0To90Days+=item.getInvAge0To90Days();
				age181To270Days+=item.getInvAge181To270Days();
				age181To330Days+=item.getInvAge181To330Days();
				age271To365Days+=item.getInvAge271To365Days();
				age31To60Days+=item.getInvAge31To60Days();
				age331To365Days+=item.getInvAge331To365Days();
				age365PlusDays+=item.getInvAge365PlusDays();
				age61To90Days+=item.getInvAge61To90Days();
				age91To180Days+=item.getInvAge91To180Days();
				if(item.getEstimatedLtsfNextCharge()!=null)longStroageFee=longStroageFee.add(item.getEstimatedLtsfNextCharge());
				if(item.getEstimatedStorageCostNextMonth()!=null)longStroageMonthFee=longStroageMonthFee.add(item.getEstimatedStorageCostNextMonth());
				if(item.getEstimatedExcessQuantity()!=null)estimatedExcessQuantity+=item.getEstimatedExcessQuantity();
				if(item.getUnitsShippedT7()!=null)unitsShippedT7+=item.getUnitsShippedT7();
				if(item.getUnitsShippedT30()!=null)unitsShippedT30+=item.getUnitsShippedT30();
				if(item.getUnitsShippedT60()!=null)unitsShippedT60+=item.getUnitsShippedT60();
				if(item.getUnitsShippedT90()!=null)unitsShippedT90+=item.getUnitsShippedT90();
				if(i==0) {
					summary.setCurrency(item.getCurrency());
				}
			}
			summary.setInvAge0To30Days(age0To30Days);
			summary.setInvAge0To90Days(age0To90Days);
			summary.setInvAge181To270Days(age181To270Days);
			summary.setInvAge181To330Days(age181To330Days);
			summary.setInvAge271To365Days(age271To365Days);
			summary.setInvAge31To60Days(age31To60Days);
			summary.setInvAge331To365Days(age331To365Days);
			summary.setInvAge365PlusDays(age365PlusDays);
			summary.setInvAge61To90Days(age61To90Days);
			summary.setInvAge91To180Days(age91To180Days);
			summary.setEstimatedExcessQuantity(estimatedExcessQuantity);
			summary.setEstimatedLtsfNextCharge(longStroageFee);
			summary.setEstimatedStorageCostNextMonth(longStroageMonthFee);
			summary.setUnitsShippedT7(unitsShippedT7);
			summary.setUnitsShippedT30(unitsShippedT30);
			summary.setUnitsShippedT60(unitsShippedT60);
			summary.setUnitsShippedT90(unitsShippedT90);
			return summary;
		}else {
			return null;
		}
	}
}
