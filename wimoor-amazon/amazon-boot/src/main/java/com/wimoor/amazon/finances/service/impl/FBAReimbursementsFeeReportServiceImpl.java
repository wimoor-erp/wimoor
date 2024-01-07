package com.wimoor.amazon.finances.service.impl;

import com.wimoor.amazon.finances.pojo.entity.FBAReimbursementsFeeReport;
import com.wimoor.amazon.finances.mapper.FBAReimbursementsFeeReportMapper;
import com.wimoor.amazon.finances.service.IFBAReimbursementsFeeReportService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-17
 */
@Service
public class FBAReimbursementsFeeReportServiceImpl extends ServiceImpl<FBAReimbursementsFeeReportMapper, FBAReimbursementsFeeReport> implements IFBAReimbursementsFeeReportService {

	@Override
	public Page<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> parameter) {
		return this.baseMapper.findByCondition(page,parameter);
	}

	
	@Override
	public void getDownloadList(SXSSFWorkbook workbook,Map<String, Object> parameter) {
		List<Map<String, Object>> list = this.baseMapper.findByCondition(parameter);
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("gname", "店铺");
		titlemap.put("marketname", "站点");
		titlemap.put("sku", "SKU");
		titlemap.put("asin", "ASIN");
		titlemap.put("approval_date", "费用时间");
		titlemap.put("amazon_order_id", "订单ID");
		titlemap.put("reimbursement_id", "报销ID");
		titlemap.put("amount_per_unit", "单个金额");
		titlemap.put("amount_total", "总金额");
		titlemap.put("currency_unit", "币种");
		titlemap.put("quantity_reimbursed_cash", "已偿还现金数量");
		titlemap.put("quantity_reimbursed_inventory", "已报销库存数量");
		titlemap.put("quantity_reimbursed_total", "已报销数量总计");
		
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
