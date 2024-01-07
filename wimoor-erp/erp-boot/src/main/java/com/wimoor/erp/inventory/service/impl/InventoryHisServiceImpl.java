package com.wimoor.erp.inventory.service.impl;


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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.erp.inventory.mapper.InventoryHisDayMapper;
import com.wimoor.erp.inventory.mapper.InventoryHisMapper;
import com.wimoor.erp.inventory.mapper.InventoryMapper;
import com.wimoor.erp.inventory.mapper.InventoryRecordMapper;
import com.wimoor.erp.inventory.pojo.dto.InvDayDetailDTO;
import com.wimoor.erp.inventory.pojo.entity.InventoryHis;
import com.wimoor.erp.inventory.service.IInventoryHisService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
 
 

@Service("inventoryHisService")
@RequiredArgsConstructor
public class InventoryHisServiceImpl  extends ServiceImpl<InventoryHisMapper,InventoryHis> implements IInventoryHisService {
	
	final InventoryHisDayMapper inventoryHisDayMapper;
	final InventoryMapper inventoryMapper;
    final InventoryRecordMapper inventoryRecordMapper;
	@Override
	public boolean updateAll(InventoryHis entity) throws BizException {
		// TODO Auto-generated method stub
		QueryWrapper<InventoryHis> updateWrapper = new QueryWrapper<InventoryHis>();
		updateWrapper.eq("warehouseid",entity.getWarehouseid());
		updateWrapper.eq("shopid",entity.getShopid());
		updateWrapper.eq("materialid",entity.getMaterialid());
		updateWrapper.eq("status",entity.getStatus());
		updateWrapper.eq("modifyday",GeneralUtil.formatDate(entity.getModifyday()));
		return   this.update(entity, updateWrapper);
		
	}
	
 
	public InventoryHis selectOne(InventoryHis entity) throws BizException {
		// TODO Auto-generated method stub
		QueryWrapper<InventoryHis> queryWrapper = new QueryWrapper<InventoryHis>();
		queryWrapper.eq("warehouseid",entity.getWarehouseid());
		queryWrapper.eq("shopid",entity.getShopid());
		queryWrapper.eq("materialid",entity.getMaterialid());
		queryWrapper.eq("status",entity.getStatus());
		queryWrapper.eq("modifyday",GeneralUtil.formatDate(entity.getModifyday()));
		List<InventoryHis> invhis = super.baseMapper.selectList(queryWrapper);
		if(invhis!=null&&invhis.size()>0)return invhis.get(0);
		return   null;
	}


	@Override
	public void summaryInvEveryDay(String byday,String endday) {
		//记录每个店铺下的本地SKU 每天库存历史
		inventoryHisDayMapper.summaryInvEveryDay(byday,endday);
	}


	@Override
	public List<Map<String, Object>> findInvDayList(Map<String, Object> maps) {
		String fromDate=maps.get("fromDate").toString();
		String toDate=maps.get("toDate").toString();
		toDate=toDate.substring(0, 10);
		fromDate=fromDate.substring(0, 10);
		maps.put("fromDate",fromDate);
		maps.put("toDate",toDate);
		String shopid=maps.get("shopid").toString();
		String materialid=maps.get("materialid")!=null?maps.get("materialid").toString():null;
		String warehouseid=maps.get("warehouseid")!=null?maps.get("warehouseid").toString():null;
		List<Map<String, Object>> dayresult = inventoryHisDayMapper.findInvDayList(maps);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if(dayresult==null||dayresult.size()==0) {
			HashMap<String, Object> item = new HashMap<String,Object>();
			item.put("fulfillable"	, 0);
			result.add(item);
			HashMap<String, Object> item2 = new HashMap<String,Object>();
			item2.put("fulfillable"	, 0);
			result.add(item2);
		}else {
			for(Map<String, Object> item:dayresult) {
				if(item==null) {
					item = new HashMap<String,Object>();
					item.put("fulfillable"	, 0);
					result.add(item);
				}else {
					result.add(item);
				}
			}
		}
		if(toDate.equals(GeneralUtil.formatDate(new Date()))) {
			  List<Map<String, Object>> endlist = inventoryRecordMapper.findSkuNowInventory(shopid,materialid,warehouseid);
			  if(result.size()>=2&&endlist.size()>0) {
				  Map<String, Object> mp = endlist.get(0);
				  Map<String, Object> old = result.get(1);
				  old.putAll(mp);
			  }
			
		} 
		return result;
	}


	@Override
	public void runTask() {
		// TODO Auto-generated method stub
		Calendar c=Calendar.getInstance();
    	c.add(Calendar.DATE, -1);
    	String byday = GeneralUtil.formatDate(c.getTime());
    	Calendar c2=Calendar.getInstance();
    	String endday = GeneralUtil.formatDate(c2.getTime());
    	this.summaryInvEveryDay(byday, endday);
	}
	
	


	public List<String> getInvDayField(Map<String, Date> parameter) {
		List<String> list = new LinkedList<String>();
		Calendar calendar = Calendar.getInstance();
		Date endDate = parameter.get("endDate");
		Date beginDate = parameter.get("beginDate");
		calendar.setTime(endDate);
		for (Date step = calendar.getTime(); step.after(beginDate) || step.equals(beginDate); calendar.add(Calendar.DATE, -1), step = calendar.getTime()) {
			String field = GeneralUtil.formatDate(step, GeneralUtil.FMT_YMD);
			list.add(field);
		}
		return list;
	}
	
	public IPage<Map<String, Object>> getInvDayDetail(InvDayDetailDTO query,Map<String, Object> parameter) {
		// 整理日期参数
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
		List<String> fieldlist = getInvDayField(pmap);
		parameter.put("fieldlist", fieldlist);
		IPage<Map<String, Object>> list = null;
		if(StrUtil.isBlankOrUndefined(query.getSort())
				||query.getSort().equals("`sku`")
				||query.getSort().equals("warehouse")) {
			 list = this.inventoryHisDayMapper.getInvDayDetail(query.getPage(),parameter);
		}else {
			  List<Map<String, Object>> listAll = this.inventoryHisDayMapper.getInvDayDetail(parameter);
			  list = query.getListPage(listAll);
		}
		 if(query.getCurrentpage()==1) {
			 Map<String, Object> summary= this.inventoryHisDayMapper.getInvDayTotal(parameter);
			 if(list!=null&&list.getRecords().size()>0&&summary!=null) {
				 list.getRecords().get(0).put("summary", summary);
			 }
		 }
		return list;
	}


	@Override
	public void downloadFBAInvDayDetail(SXSSFWorkbook workbook, Map<String, Object> parameter) {
		// TODO Auto-generated method stub
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
		List<String> fieldlist = getInvDayField(pmap);
		parameter.put("fieldlist", fieldlist);
		List<Map<String, Object>> list = this.inventoryHisDayMapper.getInvDayDetail(parameter);
		//操作Excel
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sku", "SKU");
		titlemap.put("warehouse", "仓库");
		titlemap.put("mname", "名称");
		for( String itemfield:fieldlist) {
			titlemap.put(itemfield,itemfield);
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
	


 
}
