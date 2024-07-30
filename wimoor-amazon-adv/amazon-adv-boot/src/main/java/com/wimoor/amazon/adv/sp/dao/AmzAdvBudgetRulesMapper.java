package com.wimoor.amazon.adv.sp.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.wimoor.amazon.adv.controller.pojo.dto.QueryForList;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvBudgetRules;
import com.wimoor.amazon.base.BaseMapper;

@Mapper
public interface AmzAdvBudgetRulesMapper extends BaseMapper<AmzAdvBudgetRules>{

	PageList<Map<String,Object>> list(@Param("param")QueryForList param, PageBounds pageBounds);

}
