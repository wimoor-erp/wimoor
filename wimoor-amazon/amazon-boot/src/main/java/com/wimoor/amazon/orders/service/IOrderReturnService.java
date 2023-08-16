package com.wimoor.amazon.orders.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersReturnDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersReturnVo;

public interface IOrderReturnService {
	IPage<AmazonOrdersReturnVo> selectReturnsList(AmazonOrdersReturnDTO condition);

	public void downloadReturnlist(SXSSFWorkbook workbook,AmazonOrdersReturnDTO condition);
}
