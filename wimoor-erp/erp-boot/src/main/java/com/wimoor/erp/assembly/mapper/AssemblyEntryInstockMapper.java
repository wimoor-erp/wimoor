package com.wimoor.erp.assembly.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.assembly.pojo.entity.AssemblyEntryInstock;

@Mapper
public interface AssemblyEntryInstockMapper extends BaseMapper<AssemblyEntryInstock> {
	List<Map<String,Object>> selectByFormId(@Param("formid")String formid);
	Integer findhasAssemblyFormNum(@Param("shipmentid")String shipmentid);
	List<Map<String,Object>> findProcessHandle(Map<String,Object> param);
}