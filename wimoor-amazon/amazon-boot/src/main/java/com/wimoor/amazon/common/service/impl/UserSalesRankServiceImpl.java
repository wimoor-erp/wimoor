package com.wimoor.amazon.common.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.common.mapper.UserSalesRankMapper;
import com.wimoor.amazon.common.pojo.entity.UserSalesRank;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.common.service.IUserSalesRankService;
@Service("userSalesRankService")
public class UserSalesRankServiceImpl extends  ServiceImpl<UserSalesRankMapper, UserSalesRank> implements IUserSalesRankService {
	
	@Resource
	IExchangeRateHandlerService exchangeRateHandlerService;
	
	
	public void summaryAllUserSales() {
		Map<String,Object> param=new HashMap<String,Object>();
		Map<String,BigDecimal> map=exchangeRateHandlerService.currencyChangeRate("CNY");
		param.put("currencyrate", map);
		this.baseMapper.summaryAllUserSales(param);
	}
	
	public Date getSummaryLastDate() {
		return this.baseMapper.summaryLastDate();
	}

	@Override
	public IPage<Map<String, Object>> findRankByShop(Page<?> page, String shopid, String daytype) {
		return this.baseMapper.findRankByShop(page,shopid,daytype);
	}
	
	
	
}
