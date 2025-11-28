package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.finances.pojo.entity.AmzSettlementSummarySkuMonth;

import java.util.Date;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
