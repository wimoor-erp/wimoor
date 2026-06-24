package com.wimoor.amazon.profit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface ProfitConfigMapper  extends BaseMapper<ProfitConfig> {
	
    List<ProfitConfig> findPlanNames(String shopId);

	List<Map<String, String>> findConfigByPrimaryKey(String id);

	long selectProfitPlanCountByShopId(String shopId);

	int setDefaultPlanById(String id);

	ProfitConfig findDefaultPlan(String shopId);

	int updateAllByShopId(String shopId);

	ProfitConfig findSystemProfitCfg();

	ProfitConfig findDefaultPlanIdByGroup(String groupid);

    ProfitConfig findFinDefaultPlanIdByGroup(String groupid);
}
