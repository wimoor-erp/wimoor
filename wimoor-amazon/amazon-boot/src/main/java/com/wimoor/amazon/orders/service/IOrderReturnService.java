package com.wimoor.amazon.orders.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;

import java.util.List;
import java.util.Map;

public interface IOrderReturnService {
	IPage<AmazonOrdersReturnVo> selectReturnsList(AmazonOrdersReturnDTO condition);

	public void downloadReturnlist(SXSSFWorkbook workbook,AmazonOrdersReturnDTO condition);

	IPage<Map<String,Object>> selectReturnsListBySKU(AmazonOrdersReturnDTO condition);
	Map<String,Object> selectReturnsSummaryByDay(AmazonOrdersReturnDTO condition);
	List<Map<String, Object>> selectReturnsSummaryByType(AmazonOrdersReturnDTO condition);
}
