package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.common.mvc.BizException;
 

@Service("EUProfitService")  
public class EUProfitServiceImpl extends UKProfitServiceImpl{
	
	@Override
	public BigDecimal calculateStorageFee(String country, ProfitConfigCountry profitConfigX, InputDimensions inputDimension, boolean isStandard) throws BizException {//每月每立方英尺，按比例收费
		BigDecimal storageFee =new BigDecimal("0");
		String warehousesite=profitConfigX.getWarehousesite();
		if("uk".equals(warehousesite)){//统一从英国站点配送，按英国站点的仓储费计算，需要单位转换
			storageFee = super.calculateStorageFee(country, profitConfigX, inputDimension, isStandard);
			storageFee = super.exchangeRateHandlerService.changeCurrencyByLocal("GBP", getCurrencyUnit(country), storageFee);
		} else {
			BigDecimal monthlyFee =  profitConfigX.getStoragefee();
			BigDecimal month = profitConfigX.getAmonthBigDecimal();
			BigDecimal volume = inputDimension.getVolume(InputDimensions.unit_cm).getValue().divide(new BigDecimal("1000000"),
					4, RoundingMode.HALF_UP);//  立方厘米转换成立方米
			storageFee = monthlyFee.multiply(volume).multiply(month);
		}
		return storageFee;
	}
	
}
