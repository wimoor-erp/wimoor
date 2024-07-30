package com.wimoor.amazon.finances.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.finances.mapper.FBALongTermStorageFeeReportMapper;
import com.wimoor.amazon.finances.mapper.FBAStorageFeeReportMapper;
import com.wimoor.amazon.finances.pojo.entity.FBALongTermStorageFeeReport;
import com.wimoor.amazon.finances.service.IAmzStorageFeeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */
@Service
public class AmzStorageFeeServiceImpl extends ServiceImpl<FBALongTermStorageFeeReportMapper,FBALongTermStorageFeeReport> implements IAmzStorageFeeService {

	@Autowired
	FBAStorageFeeReportMapper fBAStorageFeeReportMapper;
	
	@Override
	public Page<Map<String, Object>> findByCondition(IPage<?> page, Map<String, Object> map) {
		return this.baseMapper.findByCondition(page, map);
	}

	@Override
	public Page<Map<String, Object>> findStorageFeeList(IPage<?> page, Map<String, Object> map) {
		return fBAStorageFeeReportMapper.findStorageFeeList(page, map);
	}

	@Override
	public void getDownloadList(SXSSFWorkbook workbook, Map<String, Object> parameter) {
		List<Map<String, Object>> list = fBAStorageFeeReportMapper.findStorageFeeList(parameter);
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("gname", "店铺");
		titlemap.put("marketname", "站点");
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("month", "费用时间");
		titlemap.put("name", "商品名称");
		titlemap.put("fulfillment_center", "配送中心");
		titlemap.put("monthly_storage_fee", "月度仓储费");
		titlemap.put("currency", "币种");
		titlemap.put("average_quantity_on_hand", "平均现有数量");
		titlemap.put("average_quantity_pending_removal", "平均数量待移除");
		titlemap.put("longest_side", "长");
		titlemap.put("median_side", "宽");
		titlemap.put("shortest_side", "高");
		titlemap.put("measurement_units", "单位");
		titlemap.put("weight", "重");
		titlemap.put("estimated_total_item_volume", "单位总体积");
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						if("weight".equals(titlearray[j].toString())) {
							if(map.get("unit_of_dimension")!=null && map.get("unit_of_dimension").toString().equals("inches")) {
								cell.setCellValue(value.toString()+"磅");
							}else {
								cell.setCellValue(value.toString()+"千克");
							}
						}else {
							cell.setCellValue(value.toString());
						}
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getDownloadLongList(SXSSFWorkbook workbook, Map<String, Object> parameter) {
		List<Map<String, Object>> list = this.baseMapper.findByCondition(parameter);
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("gname", "店铺");
		titlemap.put("marketname", "站点");
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("snapshot_date", "费用时间");
		titlemap.put("qty_charged", "数量");
		titlemap.put("amount_charged", "扣费");
		titlemap.put("per_unit_volume", "每单位体积");
		titlemap.put("rate_surcharge", "费率附加费");
		titlemap.put("surcharge_age_tier", "附加费天数");
		titlemap.put("currency", "币种");
		
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(0);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		} else {
			try {
				throw new Exception("没有数据可导出！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	

	 
}
