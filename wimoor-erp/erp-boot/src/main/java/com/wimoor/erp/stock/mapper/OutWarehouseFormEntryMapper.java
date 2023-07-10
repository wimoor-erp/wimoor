package com.wimoor.erp.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseFormEntry;
@Mapper
public interface OutWarehouseFormEntryMapper extends BaseMapper<OutWarehouseFormEntry> {

	List<Map<String, Object>> selectByFormid(@Param("formid") String formid);

	void deleteByFormid(String formid);

	List<Map<String, Object>> findFormDetailByFormid(String formid);

	List<Map<String, Object>> findFormEntryByFormid(String formid);
	Map<String,Object> findLast(@Param("shopid")String shopid,@Param("warehouseid") String warehouseid,@Param("materialid")String materialid);
}