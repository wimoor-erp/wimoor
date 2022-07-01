package com.wimoor.amazon.inbound.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundTrans;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface ShipInboundTransMapper extends BaseMapper<ShipInboundTrans> {
	
	IPage<Map<String, Object>> transFeeShared(Page<?> page,Map<String, Object> param);
	
	List<Map<String, Object>> transFeeShared(Map<String, Object> param);
		 
	IPage<Map<String, Object>> transFeeSharedDetail(Page<?> page,Map<String, Object> param);
	
	List<Map<String, Object>> transFeeSharedDetail(Map<String, Object> param);
	
	List<Map<String, Object>> localitem(Map<String, Object> param);

	Map<String, Object> getSelfTransDataView(String shipmentid);
	
	IPage<Map<String, Object>> transFeeSharedWeek(Page<?> page,Map<String, Object> param);
	List<Map<String, Object>> transFeeSharedWeek(Map<String, Object> param);
}