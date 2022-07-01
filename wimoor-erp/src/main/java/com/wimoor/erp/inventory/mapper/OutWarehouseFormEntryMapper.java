package com.wimoor.erp.inventory.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.inventory.pojo.entity.OutWarehouseFormEntry;
@Mapper
public interface OutWarehouseFormEntryMapper extends BaseMapper<OutWarehouseFormEntry> {

	List<Map<String, Object>> selectByFormid(String formid);

	void deleteByFormid(String formid);

	List<Map<String, Object>> findFormDetailByFormid(String formid);

	List<Map<String, Object>> findFormEntryByFormid(String formid);
}