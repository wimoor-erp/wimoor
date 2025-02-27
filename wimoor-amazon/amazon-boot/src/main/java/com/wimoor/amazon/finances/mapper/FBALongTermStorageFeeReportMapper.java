package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.FBALongTermStorageFeeReport;

@Mapper
public interface FBALongTermStorageFeeReportMapper  extends BaseMapper<FBALongTermStorageFeeReport> {
 
	Page<Map<String, Object>> findByCondition(IPage<?> page,@Param("param") Map<String, Object> map);

	List<Map<String, Object>> findByCondition(@Param("param")Map<String, Object> parameter);

	void insertBatch(List<FBALongTermStorageFeeReport> list);
	List<Map<String, Object>> findSku_LongtermStorage(Map<String, Object> map);
}

 