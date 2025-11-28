package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
 
@Mapper
public interface AmzFinUserItemDataMapper  extends BaseMapper<AmzFinUserItemData>{

	IPage<Map<String, Object>> getFinDataList(Page<?>page,@Param("param") Map<String, Object> params);
	
	List<Map<String, Object>> getFinDataList(@Param("param")Map<String, Object> params);
	
	List<Map<String, Object>> getFinDataLastWeek(@Param("list")List<Map<String, Object>> list);
	
	IPage<Map<String, Object>> getFinDataMonthList(Page<?>page,@Param("param")Map<String, Object> params);
	
	List<Map<String, Object>> getFinDataMonthList(@Param("param")Map<String, Object> params);
		
	void insertBatch(List<AmzFinUserItemData> list);
	
	List<Map<String, Object>> getFinDataForSku(Map<String, Object> params);
}