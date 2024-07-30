package com.wimoor.amazon.adv.report.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumAllTypeKey;
import com.wimoor.amazon.base.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmzAdvSumAllTypeMapper  extends BaseMapper<AmzAdvSumAllType>{
	AmzAdvSumAllType  selectByKey(AmzAdvSumAllTypeKey key);
/*    int deleteByPrimaryKey(AmzAdvSumAllTypeKey key);

    int insert(AmzAdvSumAllType record);

    int insertSelective(AmzAdvSumAllType record);

    AmzAdvSumAllType selectByPrimaryKey(AmzAdvSumAllTypeKey key);

    int updateByPrimaryKeySelective(AmzAdvSumAllType record);

    int updateByPrimaryKey(AmzAdvSumAllType record);*/

	int updateByKey(AmzAdvSumAllType typesum);
	 
	List<Map<String, Object>> getMonthSumNum(Map<String,Object> param);
	List<Map<String, Object>> getMonthSumAsinNum(Map<String,Object> param);
	List<Map<String, Object>> getTypeNumberWithoutUnUseable(@Param("shopid")String shopid,@Param("byday")String byday);
	List<Map<String, Object>> getTypeNumberEnabled(@Param("shopid")String shopid,@Param("byday")String byday);
}