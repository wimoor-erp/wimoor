package com.wimoor.amazon.orders.service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.orders.pojo.entity.SummaryAll;

public interface ISummaryAllService extends IService<SummaryAll> {
	public BigDecimal orderSummaryAll(Map<String, Object> param) ;

	public Map<String, Object> orderMonthsSummaryAll(Map<String, Object> param) ;

	public Map<String, Object> orderDaysSummaryAll(Map<String, Object> param);
}
