package com.wimoor.amazon.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.common.pojo.entity.DaysalesFormula;

public interface IDaysalesFormulaService extends IService<DaysalesFormula>{
	public DaysalesFormula selectByShopid(String shopid);
}
