package com.wimoor.amazon.finances.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySkuMonth;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-26
 */
public interface IAmzSettlementSummarySkuMonthService extends IService<AmzSettlementSummarySkuMonth> {

	void summaryMonthly(String amazonauthid, String marketplaceName, Date settlementStartDate);

	IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map);

	void getDownloadList(SXSSFWorkbook workbook, Map<String, Object> map);

	List<Map<String,Object>> summaryQuarter(Map<String, Object> param);
	List<Map<String, Object>> getSummaryMonth(Map<String, Object> param);

	List<Map<String, Object>> getSummaryMonthStorage(Map<String, Object> param);

    void getDownloadListOld(SXSSFWorkbook workbook, Map<String, Object> map);
}
