package com.wimoor.amazon.common.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.pojo.entity.ExchangeRateCustomer;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;

 
public interface IExchangeRateService extends IService<ExchangeRate> {
	public void updateExchangeRate() throws BizException;
	public List<ExchangeRate> getExchangeRateLimit();
	public List<Map<String,Object>> getMyCurrencyRate(String shopid,String byday);
	public ExchangeRate selectByName(String from);
	public ExchangeRate selectMineByName(String from, String shopid);
	public void saveMyCurrencyRate(UserInfo user,ExchangeRateCustomer rate) ;
	public void setExcelBook(SXSSFWorkbook workbook, List<Map<String, Object>> list);
}
