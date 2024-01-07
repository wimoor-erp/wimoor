package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.FBAStorageFeeReport;
@Mapper
public interface FBAStorageFeeReportMapper extends BaseMapper<FBAStorageFeeReport> {

	void insertBatch(List<FBAStorageFeeReport> linkedlist);
	
	Page<Map<String, Object>> findStorageFeeList(IPage<?> page,@Param("param") Map<String, Object> map);

	List<Map<String, Object>> findStorageFeeList(@Param("param")Map<String, Object> parameter);
	
	List<Map<String, Object>> findSku_StorageFee(Map<String, Object> map);
}
 
