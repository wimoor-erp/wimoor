package com.wimoor.amazon.inventory.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.inventory.mapper.AmzInventoryCountryReportMapper;
import com.wimoor.amazon.inventory.mapper.AmzInventoryPlanningMapper;
import com.wimoor.amazon.inventory.mapper.InventoryReportHisMapper;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.pojo.dto.InventoryPlanningDTO;
import com.wimoor.amazon.inventory.pojo.entity.InventoryReport;
import com.wimoor.amazon.inventory.pojo.vo.AmzInventoryPlanningVo;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.inventory.service.IFBAInventoryService;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.report.pojo.dto.InvDayDetailDTO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FBAInventoryServiceImpl extends ServiceImpl<InventoryReportMapper,InventoryReport> implements IFBAInventoryService{
	
	final IProductInfoService iProductInfoService;
	
	final AmzInventoryPlanningMapper amzInventoryPlanningMapper;
	final ProductInfoMapper productInfoMapper;
	final ErpClientOneFeignManager erpClientOneFeign;
	final AmzInventoryCountryReportMapper amzInventoryCountryReportMapper;
	final InventoryReportHisMapper inventoryReportHisMapper;
	public List<Map<String, Object>> findByTypeWithStockCycle(String msku, String shopid) {
		List<Map<String, Object>> list =this.baseMapper.findFBAWithStockCycle(msku, shopid);
		return list;
	}	
	public Map<String, Object> findSum(Map<String,Object> param) {
		return this.baseMapper.findFBASum(param);
	}
	
	public Map<String, Object> findFBAInvDetailById(String sku, String warehouseid, String shopid,String groupid) {
		return this.baseMapper.findFBAInvDetailById(sku, warehouseid, shopid,groupid);
	}
	
	@Override
	public List<Map<String, String>> getInvDaySumField(Map<String, Date> parameter) { 
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		Calendar calendar = Calendar.getInstance();
		Date endDate = parameter.get("endDate");
		Date beginDate = parameter.get("beginDate");
		calendar.setTime(endDate);
		for (Date step = calendar.getTime(); step.after(beginDate) || step.equals(beginDate); calendar.add(Calendar.DATE, -1), step = calendar.getTime()) {
			String field = GeneralUtil.formatDate(step, GeneralUtil.FMT_YMD);
			Map<String, String> map = new HashMap<String, String>();
			map.put("byday", field);
			map.put("field", "v" + field);
			list.add(map);
		}
		return list;
	}
	public Map<String, Object> getFBAInvDayDetailTotal(Map<String, Object> parameter) {
		return inventoryReportHisMapper.getFBAInvDayTotal(parameter);
	}
	public IPage<Map<String, Object>> getFBAInvDayDetail(InvDayDetailDTO dto,Map<String, Object> parameter) {
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

		List<Map<String, Object>> list = inventoryReportHisMapper.getFBAInvDayDetail(parameter);
		IPage<Map<String, Object>> pagelist = dto.getListPage(list);
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
					pagemap.put("image", image_location);
					pagemap.put("pname", product.get("name"));
				}
			}
		}
		return pagelist;
	}
	
	@Override
	public void downloadFBAInvDayDetail(SXSSFWorkbook workbook,Map<String, Object> parameter) {
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

		List<Map<String, Object>> list = inventoryReportHisMapper.getFBAInvDayDetail(parameter);
		if (list != null && list.size()> 0) {
			String marketplaceid = null;
			String groupid = null;
			if (parameter.get("warehouse") != null) {
				marketplaceid = parameter.get("warehouse").toString();
			}
			if (parameter.get("groupid") != null) {
				groupid = parameter.get("groupid").toString();
			}
			for (Map<String, Object> pagemap : list) {
				String sku_p = pagemap.get("sku").toString();
				Map<String, Object> product = iProductInfoService.findNameAndPicture(sku_p, marketplaceid, groupid);
				if (product != null) {
					pagemap.put("pname", product.get("name"));
				}
			}
		}
			//操作Excel
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("sku", "SKU");
			titlemap.put("warehouse", "仓库");
			titlemap.put("pname", "名称");
			for(Map<String, String> itemfield:fieldlist) {
				titlemap.put(itemfield.get("field").toString(),itemfield.get("byday"));
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
				Map<String, Object> item  = list.get(i);
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
	
	public IPage<AmzInventoryPlanningVo> selectPlanPageList(InventoryPlanningDTO dto){
		IPage<AmzInventoryPlanningVo> pagelist = amzInventoryPlanningMapper.selectPageList(dto.getPage(), dto);
		if(pagelist!=null && pagelist.getRecords()!=null && pagelist.getRecords().size()>0) {
			List<AmzInventoryPlanningVo> list = pagelist.getRecords();
			 List<String> skulist=new LinkedList<String>();
			 for (int i = 0; i < list.size(); i++) {
				 AmzInventoryPlanningVo item = list.get(i);
				 skulist.add(item.getMsku());
			}
			Result<Map<String, Object>> res = erpClientOneFeign.findMaterialMapBySku(skulist);
			if(Result.isSuccess(res)&&res.getData()!=null) {
				Map<String, Object> result = res.getData();
				 for (int i = 0; i < list.size(); i++) {
					 AmzInventoryPlanningVo item = list.get(i);
					 @SuppressWarnings("unchecked")
					 Map<String,Object> skuinfo =result.get(item.getMsku())!=null? (Map<String, Object>) result.get(item.getMsku()):null;
					 if(skuinfo!=null && skuinfo.get("price")!=null) {
							String purchasecost = skuinfo.get("price").toString();
							BigDecimal cost=new BigDecimal(purchasecost);
							item.setPurchasecost(cost);
							String name = skuinfo.get("name").toString();
							item.setPname(name);
						}
				 }
			}
		}
		return pagelist;
	}
	String getValueToString(Integer value) {
		if(value==null)return "0";
		else return value.toString();
	}
	@Override
	public void downloadPlanList(SXSSFWorkbook workbook, InventoryPlanningDTO dto) {
		 List<AmzInventoryPlanningVo> list = amzInventoryPlanningMapper.selectPageList(dto);
		 if(list!=null && list.size()>0) {
			 List<String> skulist=new LinkedList<String>();
			 for (int i = 0; i < list.size(); i++) {
				 AmzInventoryPlanningVo item = list.get(i);
				 skulist.add(item.getMsku());
			}
			Result<Map<String, Object>> res = erpClientOneFeign.findMaterialMapBySku(skulist);
			if(Result.isSuccess(res)&&res.getData()!=null) {
				Map<String, Object> result = res.getData();
				 for (int i = 0; i < list.size(); i++) {
					 AmzInventoryPlanningVo item = list.get(i);
					 @SuppressWarnings("unchecked")
					 Map<String,Object> skuinfo =result.get(item.getMsku())!=null? (Map<String, Object>) result.get(item.getMsku()):null;
					 if(skuinfo!=null && skuinfo.get("price")!=null) {
							String purchasecost = skuinfo.get("price").toString();
							BigDecimal cost=new BigDecimal(purchasecost);
							item.setPurchasecost(cost);
						}
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
			titlemap.put("salesShippedLast7Days",  "最近7天销售额(站点币种)");
			titlemap.put("salesShippedLast30Days", "最近30天销售额(站点币种)");
			titlemap.put("salesShippedLast60Days", "最近60天销售额(站点币种)");
			titlemap.put("salesShippedLast90Days", "最近90天销售额(站点币种)");
			
			titlemap.put("quantityToBeChargedAis181T210Days", "180-210天超龄库存附加费数量");
			titlemap.put("quantityToBeChargedAis211T240Days", "211-240天超龄库存附加费数量");
			titlemap.put("quantityToBeChargedAis241T270Days", "241-270天超龄库存附加费数量");
			titlemap.put("quantityToBeChargedAis271T300Days", "271-300天超龄库存附加费数量");
			titlemap.put("quantityToBeChargedAis301T330Days", "301-330天超龄库存附加费数量");
			titlemap.put("quantityToBeChargedAis331T365Days", "331-365天超龄库存附加费数量");
			titlemap.put("quantityToBeChargedAis365PlusDays", "365天以上超龄库存附加费数量");
			
			titlemap.put("estimatedAis181T210Days", "预计181-210天超龄库存附加费");
			titlemap.put("estimatedAis211T240Days", "预计211-240天超龄库存附加费");
			titlemap.put("estimatedAis241T270Days", "预计241-270天超龄库存附加费");
			titlemap.put("estimatedAis271T300Days", "预计271-300天超龄库存附加费");
			titlemap.put("estimatedAis301T330Days", "预计301-330天超龄库存附加费");
			titlemap.put("estimatedAis331T365Days", "预计331-365天超龄库存附加费");
			titlemap.put("estimatedAis365PlusDays", "预计365天以上超龄库存附加费");
			
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
					Map<String, Object> map = BeanUtil.beanToMap(item);
					for (int j = 0;j < titlearray.length; j++) {
						Cell cell = row.createCell(j); 
						String field=titlearray[j].toString();
						   if(map.get(field)!=null) {
				            	cell.setCellValue(map.get(field).toString());
							} 
					}
			    } 
			 
		 }
	}

	@Override
	public AmzInventoryPlanningVo summaryPlanning(InventoryPlanningDTO dto) {
		   AmzInventoryPlanningVo summary = amzInventoryPlanningMapper.selectPageListSummary(dto);
		   return summary;
	}
	@Override
	public IPage<ProductInventoryVo> findFba(Page<Object> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.baseMapper.findFBA(page, param);
	}
	
	@Override
	public IPage<Map<String,Object>> findFbaCountry(Page<Object> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return amzInventoryCountryReportMapper.findFBACountry(page, param);
	}
	
	@Override
	public List<ProductInventoryVo> findFba(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.baseMapper.findFBA(param);
	}
	@Override
	public void getExcelBookBAInventoryReport(SXSSFWorkbook workbook, List<ProductInventoryVo> inventoryList) {
		// TODO Auto-generated method stub
		Sheet sheet = workbook.createSheet("sheet1");
		List<String> titlelist = new ArrayList<String>();
		Map<String, String> titlechange = new HashMap<String, String>();
		titlelist.add("店铺");
		titlelist.add("FBA仓库");
		titlelist.add("SKU");
		titlelist.add("库存");
		titlelist.add("可售");
		titlelist.add("不可售");
		titlelist.add("预留");
		titlelist.add("正在发货");
		titlelist.add("待接收");
		titlelist.add("正在接收");
		titlelist.add("异常");

		titlechange.put("店铺", "groupname");
		titlechange.put("FBA仓库", "warehouse");
		titlechange.put("SKU", "sku");
		titlechange.put("库存", "afnWarehouseQuantity");
		titlechange.put("可售", "afnFulfillableQuantity");
		titlechange.put("不可售", "afnUnsellableQuantity");
		titlechange.put("预留", "afnReservedQuantity");
		titlechange.put("正在发货", "afnInboundWorkingQuantity");
		titlechange.put("待接收", "afnInboundShippedQuantity");
		titlechange.put("正在接收", "afnInboundReceivingQuantity");
		titlechange.put("异常", "afnResearchingQuantity");

		// 在索引0的位置创建行（最顶端的行）
		Row row = sheet.createRow(0);
		for (int i = 0; i < titlelist.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(titlelist.get(i));
		}

		for (int i = 0; i < inventoryList.size(); i++) {
			Row skurow = sheet.createRow(i + 1);
			ProductInventoryVo skuvo = inventoryList.get(i);
			Map<String, Object> skumap = BeanUtil.beanToMap(skuvo);
			for (int j = 0; j < titlelist.size(); j++) {
				Cell cell = skurow.createCell(j);
				if (skumap.get(titlechange.get(titlelist.get(j))) == null) {
					cell.setCellValue("-");
				} else {
					cell.setCellValue(skumap.get(titlechange.get(titlelist.get(j))).toString());
				}
			}
		}
	}
	@Override
	public IPage<Map<String, Object>> selectInventoryCost(Page<?> page, String groupid, String marketplaceid,String sku, String shopid, String byday) {
		// TODO Auto-generated method stub
		if (StrUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = "%" + sku + "%";
		}
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		IPage<Map<String,Object>> pagelist = this.baseMapper.findInventoryCost(page,groupid, marketplaceid, sku, shopid, byday);
		Map<String, Object> map = this.baseMapper.findInventoryCostTotal(groupid, marketplaceid, sku, shopid, byday);
		if (pagelist != null&&pagelist.getRecords().size() > 0 ) {
			for (Map<String, Object> pagemap : pagelist.getRecords()) {
				String sku_p = pagemap.get("sku").toString();
				if ("EU".equals(marketplaceid)) {
					marketplaceid = null;
				}
				Map<String, Object> map_p = productInfoMapper.findNameAndPicture(sku_p, marketplaceid, groupid);
				if (map_p != null) {
					pagemap.put("image", map_p.get("image"));
					pagemap.put("name", map_p.get("name"));
				}
			}
			pagelist.getRecords().get(0).put("summary", map);
			return pagelist;
		} else {
			return pagelist;
		}
	}
	
	@Override
	public List<Map<String, Object>> selectInventoryCostAll(String groupid, String marketplaceid, String sku,String shopid, String byday) {
		// TODO Auto-generated method stub
		if (StrUtil.isEmpty(sku)) {
			sku = null;
		} else {
			sku = "%" + sku + "%";
		}
		if (StrUtil.isEmpty(marketplaceid)) {
			marketplaceid = null;
		}
		List<Map<String, Object>> pagelist = this.baseMapper.findInventoryCost(groupid, marketplaceid, sku, shopid, byday);
		return pagelist;
	}
}
