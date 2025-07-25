package com.wimoor.amazon.orders.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersRemoveDTO;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderRemoves;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;

public interface IAmzOrderRemovesService extends IService<AmzOrderRemoves>{
	
	IPage<AmazonOrdersRemoveVo> selectRemovesList(AmazonOrdersRemoveDTO condition);

	public void downloadRemoveslist(SXSSFWorkbook workbook,AmazonOrdersRemoveDTO condition);
}
