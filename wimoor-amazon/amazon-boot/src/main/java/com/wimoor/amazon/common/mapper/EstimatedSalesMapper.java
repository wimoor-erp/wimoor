package com.wimoor.amazon.common.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.common.pojo.entity.EstimatedSales;
@Mapper
public interface EstimatedSalesMapper extends BaseMapper<EstimatedSales>{
	EstimatedSales findEstimatedSales(Map<String, Object> param);
	
 
}