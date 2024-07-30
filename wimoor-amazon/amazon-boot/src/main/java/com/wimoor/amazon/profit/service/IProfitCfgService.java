package com.wimoor.amazon.profit.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.InplaceFee;
import com.wimoor.amazon.profit.pojo.entity.ManualProcessingFee;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
 
public interface IProfitCfgService  extends IService<ProfitConfig> {

	ProfitConfig findConfigAction(String id);

	int getProfitPlanCountByShopId(String shopId);

	String setDefaultPlan(String id);

	/**
	 * 查找设置的默认方案，当没有设置默认方案时，返回系统默认方案
	 * @param shopId
	 * @return
	 */
	String findDefaultPlanId(String shopId);
	
	int setNotDefault(String shopId);

	ProfitConfig getSystemProfitCfg();

	public List<ProfitConfig> findProfitCfgName(String shopId);

	String findDefaultPlanIdByGroup(String groupid);
	
	ProfitConfig findSysProfitCfg();

	String insert(ProfitConfig config);

	String update(ProfitConfig config);

	public List<InplaceFee> findInplacefee(String country);

	public List<ManualProcessingFee> findManualProcessingFee() ;
}
