package com.wimoor.erp.warehouse.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.warehouse.mapper.WhseUnsalableReportMapper;
import com.wimoor.erp.warehouse.pojo.entity.WhseUnsalableReport;
import com.wimoor.erp.warehouse.service.IWhseReportService;

import lombok.RequiredArgsConstructor;
 

 
@Service("whseReportService")
@RequiredArgsConstructor
public class WhseReportServiceImpl extends  ServiceImpl<WhseUnsalableReportMapper,WhseUnsalableReport> implements IWhseReportService {
	 
	 
	private Map<String, Object> initFbaInvData(List<Map<String, Object>> list) {
		if (list.size() > 0 && list != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				String materialid = list.get(i).get("sku").toString();
				String qty = list.get(i).get("qty").toString();
				if (GeneralUtil.isNotEmpty(materialid)) {
					map.put(materialid, qty);
				}
			}
			return map;
		} else {
			return null;
		}
	}

	public Map<String, Object> initShipData(List<Map<String, Object>> list) {
		if (list.size() > 0 && list != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				String materialid = list.get(i).get("materialid").toString();
				String qty = list.get(i).get("salesum").toString();
				if (GeneralUtil.isNotEmpty(materialid)) {
					map.put(materialid, qty);
				}
			}
			return map;
		} else {
			return null;
		}
	}

	public Map<String, Object> initInvData(List<Map<String, Object>> list) {
		if (list.size() > 0 && list != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				String materialid = list.get(i).get("materialid").toString();
				String qty = list.get(i).get("qty").toString();
				if (GeneralUtil.isNotEmpty(materialid)) {
					map.put(materialid, qty);
				}
			}
			return map;
		} else {
			return null;
		}
	}

	public Date initParamDate(Integer num) {
		Calendar c = Calendar.getInstance();
		if (num != null) {
			c.add(Calendar.DATE, num);
			Date date = c.getTime();
			return date;
		} else {
			return null;
		}
	}
 

	public void findLocalInvDead(SXSSFWorkbook workbook, Map<String, Object> params) {
		List<LinkedHashMap<String, Object>> list = this.baseMapper.getLocalDeadRpt(params);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> item = list.get(i);
			BigDecimal numnow = (BigDecimal) item.get("0~30天库龄");
			BigDecimal num30 = (BigDecimal) item.get("30~60天库龄");
			BigDecimal num60 = (BigDecimal) item.get("60~90天库龄");
			BigDecimal num90 = (BigDecimal) item.get("90~180天库龄");
			BigDecimal num180 = (BigDecimal) item.get("180~365天库龄");
			BigDecimal num365 = (BigDecimal) item.get("365天以上库龄");
			item.put("0~30天库龄", numnow.floatValue() >= num30.floatValue() ? numnow.subtract(num30) : numnow);
			item.put("30~60天库龄", num30.floatValue() >= num60.floatValue() ? num30.subtract(num60) : num30);
			item.put("60~90天库龄", num60.floatValue() >= num90.floatValue() ? num60.subtract(num90) : num60);
			item.put("90~180天库龄", num90.floatValue() >= num180.floatValue() ? num90.subtract(num180) : num90);
			item.put("180~365天库龄", num180.floatValue() >= num365.floatValue() ? num180.subtract(num365) : num180);
		}
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (i == 0) {
				int titlestep = 0;
				Row trow = sheet.createRow(i);
				for (Entry<String, Object> entry : map.entrySet()) {
					String key = entry.getKey();
					Cell titlecell = trow.createCell(titlestep++);
					if (key.contains(",")) {
						key = key.split(",")[1];
					}
					titlecell.setCellValue(key);
				}
			}
			Row row = sheet.createRow(i + 1);
			int step = 0;
			for (Entry<String, Object> entry : map.entrySet()) {
				Cell cell = row.createCell(step++);
				Object key = entry.getValue();
				if (key != null) {
					cell.setCellValue(key.toString());
				} else {
					cell.setCellValue("--");
				}
			}
		}
	}

 

	public void setChgRateExcelBook(SXSSFWorkbook workbook, Map<String, Object> param) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("name", "库存仓库");
		titlemap.put("startqty", "期初库存数量");
		titlemap.put("endqty", "期末库存数量");
		titlemap.put("salesum", "期间出库数量  / 期间销售数量");
		titlemap.put("wrate", "库存周转率");
		titlemap.put("wday", "库存周转天数");
		List<Map<String, Object>> list = this.findInvChgRateByCondition(param);
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
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
		
	}

	@Override
	public IPage<Map<String, Object>> findUnsalableReportByCondition(Page<?> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findInvChgRateByCondition(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findMaterialSizeByCondition(Map<String, Object> param) throws ERPBizException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateReprot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPage<Map<String, Object>> findFbaUnsalableReportByCondition(Page<?> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findFBAInvDead(SXSSFWorkbook workbook, Map<String, Object> params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getFbaSnapdate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPage<Map<String, Object>> findUnsalableReportByDay(Page<?> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

}
