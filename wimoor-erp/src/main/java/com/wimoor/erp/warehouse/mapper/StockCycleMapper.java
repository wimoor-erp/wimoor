package com.wimoor.erp.warehouse.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.warehouse.pojo.entity.StockCycle;
@Mapper
public interface StockCycleMapper extends BaseMapper<StockCycle> {

	List<Map<String, Object>> selectByMaterial(String materialid);

	void deleteByMaterial(String materialid);
}