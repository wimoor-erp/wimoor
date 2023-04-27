package com.wimoor.amazon.finances.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.AmzFinSettlementFormula;
import com.wimoor.common.user.UserInfo;

public interface IAmzFinSettlementFormulaService  extends IService<AmzFinSettlementFormula>{
	public Map<String,Object> getAmzFinSettlementFormulaConvert(String shopid);
	public Map<String, String> getformulaTitle(String shopid);
	public AmzFinSettlementFormula getAmzFinSettlementFormula(String shopid);
	public String saveformulaData(UserInfo user, String formuladata,String pricetype);
}
