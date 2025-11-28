package com.wimoor.amazon.adv.sp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.sp.pojo.AmzAdvCampaignBudgetRule;
import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface AmzAdvCampaignBudgetRuleMapper extends BaseMapper<AmzAdvCampaignBudgetRule>{

	List<Map<String, Object>> listRules(@Param("mylist")List<Map<String, Object>> mylist);

}
