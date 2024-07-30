package com.wimoor.amazon.orders.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.orders.pojo.entity.SummaryAll;

@Mapper
public interface SummaryAllMapper extends BaseMapper<SummaryAll> {
    int deleteByPrimaryKey(SummaryAll record);

    int insert(SummaryAll record);

    int insertSelective(SummaryAll record);
    
    List<Map<String,Object>> selectByMutilParameter(Map<String,Object> parameter);
    
    Map<String,Object> selectSumByMutilParameter(Map<String,Object> parameter);
    
    List<Map<String,Object>> selectSummary(Map<String,Object> parameter);
    
    List<Map<String,Object>> selectDaysSummary(Map<String,Object> parameter);
    
    int updateByPrimaryKeySelective(SummaryAll record);

    int updateByPrimaryKey(SummaryAll record);
 
    List<SummaryAll> selectByKey(SummaryAll sumall);

	void saveBatch(List<SummaryAll> list);

	List<Map<String, Object>> selectMonthsSummary(Map<String, Object> param);

	List<Map<String, Object>> marketSummary(Map<String,Object> parameter);

	Map<String, Object> selectLastByMutilParameter(Map<String, Object> map);
}