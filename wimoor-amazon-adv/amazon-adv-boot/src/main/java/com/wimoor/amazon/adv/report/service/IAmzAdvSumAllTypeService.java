package com.wimoor.amazon.adv.report.service;

import java.util.List;
import java.util.Map;

import com.wimoor.amazon.adv.common.service.IService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllTypeKey;
 

public interface IAmzAdvSumAllTypeService extends IService<AmzAdvSumAllType>{

	int updateByKey(AmzAdvSumAllType typesum);

	int insert(AmzAdvSumAllType typesum);
	public AmzAdvSumAllType selectByKey(AmzAdvSumAllTypeKey key);

	public Map<String, Object> getTypeNumber(String shopid);
	public Map<String, Object> getEnableNumber(String shopid);
	public List<Map<String, Object>> getMonthSumAdgroupNum(Map<String, Object> param);
	public List<Map<String, Object>> getMonthSumAsinNum(Map<String, Object> param);
}
