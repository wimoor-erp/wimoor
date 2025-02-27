package com.wimoor.erp.inventory.service.impl;

import java.util.HashMap;
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
import com.wimoor.erp.common.mapper.FormTypeMapper;
import com.wimoor.erp.common.pojo.entity.FormType;
import com.wimoor.erp.inventory.mapper.InventoryRecordMapper;
import com.wimoor.erp.inventory.pojo.entity.InventoryRecord;
import com.wimoor.erp.inventory.service.IInventoryRecordService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("inventoryRecordService")
@RequiredArgsConstructor
public class InventoryRecordServiceImpl extends ServiceImpl<InventoryRecordMapper,InventoryRecord> implements IInventoryRecordService {
	
	final FormTypeMapper formTypeMapper;
	@Override
	public IPage<Map<String, Object>> findRecordList(Page<Object> page, Map<String, Object> maps) {
		IPage<Map<String, Object>> list=this.baseMapper.findByCondition(page,maps);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> findDownloadList(Map<String, Object> maps) {
		List<Map<String, Object>> list=this.baseMapper.findByCondition(maps);
		return list;
	}

	@Override
	public List<FormType> getFormTypeList() {
		QueryWrapper<FormType> queryWrapper=null;
		return formTypeMapper.selectList(queryWrapper);
	}

	@Override
	public void setExcelBookInventoryRecReport(SXSSFWorkbook workbook, List<Map<String, Object>> list) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("sku", "SKU");
		titlemap.put("name", "名称");
		titlemap.put("typename", "单据类型");
		titlemap.put("number", "单据编码");
		titlemap.put("operate", "操作行为");
		titlemap.put("startfulfillable", "起始可用库存");
		titlemap.put("fulfillable", "可用库存变动量");
		titlemap.put("endfulfillable", "结余可用库存");
		titlemap.put("startinbound", "起始待入库库存");
		titlemap.put("inbound", "待入库变动量");
		titlemap.put("endinbound", "结余待入库库存");
		titlemap.put("startoutbound", "起始待出库库存");
		titlemap.put("outbound", "待出库变动量");
		titlemap.put("endoutbound", "结余待出库库存");
		titlemap.put("warehousename", "操作仓库");
		titlemap.put("opttime", "操作时间");
		titlemap.put("operator", "操作人");
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
			Map<String, Object> map = list.get(i);
			for (int j = 0; j < titlearray.length; j++) {
				Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
				Object value = map.get(titlearray[j].toString());
				if (value != null) {
					if ("operate".equals(titlearray[j].toString())) {
						if ("out".equals(value)) {
							value = "出库";
						} else if("in".equals(value)) {
							value = "入库";
						}else if("readyin".equals(value)) {
							value = "准备入库";
						}else if("readyout".equals(value)) {
							value = "准备出库";
						}
					}
					cell.setCellValue(value.toString());
				}
			}
		}
	
	}

	@Override
	public Map<String, Object> findSkuInvHistory(String materialid, String fromDate, String toDate,String warehouseid, String shopid) {
		//sku期间入库和出库的数量
		Map<String, Object> result=null;
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("materialid", materialid);
		param.put("fromDate", fromDate);
		param.put("toDate", toDate);
		param.put("shopid", shopid);
		param.put("ftype", "instock");
		if(StrUtil.isEmpty(warehouseid)) {
    		param.put("warehouseid", null);
    	}else {
    		param.put("warehouseid", warehouseid);
    	}
		List<Map<String, Object>> inlist=this.baseMapper.findSkuInvHistory(param);
		param.put("ftype", "outstock");
		List<Map<String, Object>> outlist=this.baseMapper.findSkuInvHistory(param);
	
		if(inlist.size()>0 || outlist.size()>0 ) {
			result=new HashMap<String, Object>();
			if(inlist!=null &&inlist.size()>0) {
				result.put("inmap", inlist.get(0));
			}
			if(outlist!=null &&outlist.size()>0) {
				result.put("outmap", outlist.get(0));
			}
		 
		}
		return result;
	}

	
	public void downloadOutstockformOut(SXSSFWorkbook workbook,Map<String,Object> param){
		List<Map<String, Object>>  list=this.baseMapper.findOutstockformOut(param);
		 if(list!=null && list.size()>0) {
			//操作Excel
			Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
			titlemap.put("byday", "日期");
			titlemap.put("company", "公司");
			titlemap.put("warheousename", "仓库");
			titlemap.put("sku", "SKU");
			titlemap.put("name", "名称");
			titlemap.put("handle", "发货数量");
			titlemap.put("number", "对应订单");
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
					item.put("company", param.get("company"));
					Row row = sheet.createRow(i + 1);
					Map<String, Object> map =item;
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
}
