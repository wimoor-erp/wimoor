package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.finances.pojo.entity.FBAReimbursementsFeeReport;

import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-17
 */
public interface IFBAReimbursementsFeeReportService extends IService<FBAReimbursementsFeeReport> {

	Page<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> parameter);

	void getDownloadList(SXSSFWorkbook workbook, Map<String, Object> parameter);

}
