package com.wimoor.amazon.common.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.wimoor.amazon.common.mapper.DaysalesFormulaMapper;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;
import com.wimoor.amazon.common.service.IDaysalesFormulaService;

import lombok.RequiredArgsConstructor;
@Service("daysalesFormulaService")
@RequiredArgsConstructor
public class DaysalesFormulaServiceImpl  extends ServiceImpl<DaysalesFormulaMapper, DaysalesFormula> implements IDaysalesFormulaService{
	
	public static int getAvgSales(DaysalesFormula dayformula, int summonth, int sumseven, int sumhalfmonth, Date openDay) {
		int qty = 0;
		double monthday = 30.0;
		double halfmonthday = 15.0;
		double weekday = 7.0;
		double monthrate = 0.2;
		double halfmonthrate = 0.3;
		double weekdayrate = 0.5;
		if (openDay != null) {
			Calendar c = Calendar.getInstance();
			long diff = c.getTime().getTime() - openDay.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			if (days > 1) {
				days = days - 1;
			}
			if (days != 0) {
				if (days < weekday) {
					weekday = days;
					halfmonthday = days;
					monthday = days;
				} else if (days < halfmonthday) {
					halfmonthday = days;
					monthday = days;
				} else if (days < monthday) {
					monthday = days;
				}
			}
		}
		double dayquantity = 0;
		if (dayformula != null && dayformula.getFormula() != null) {
		    HashMap<String,Object> param=new HashMap<String,Object>();
		    param.put("sumhalfmonth", sumhalfmonth);
		    param.put("summonth", summonth);
		    param.put("sumseven", sumseven);
		    Expression compiledExp = AviatorEvaluator.compile(dayformula.getFormula());
			Object result = compiledExp.execute(param);
			
			dayquantity =Double.parseDouble(result.toString());
		} else {
			dayquantity = (summonth * monthrate) / (monthday) + (sumseven * weekdayrate) / (weekday)
					+ (sumhalfmonth * halfmonthrate) / (halfmonthday);
		}
		qty = (int) Math.ceil(dayquantity * 10);
		int dom = qty % 10;
		if (dom > 8) {
			qty = qty / 10 + 1;
		} else {
			qty = qty / 10;
		}
		return qty;
	}
	
	
	@Cacheable(value = "DaysalesFormulaCache",  key="#shopid")
	public DaysalesFormula selectByShopid(String shopid) {
		return this.baseMapper.selectByShopid(shopid);
	}

	 

	 
	
	
}
