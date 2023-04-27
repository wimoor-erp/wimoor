package com.wimoor.erp.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.stock.pojo.entity.ChangeWhFormEntry;
@Mapper
public interface ChangeWhFormEntryMapper extends BaseMapper<ChangeWhFormEntry>{


	List<Map<String, Object>> findFormDetailByFormid(String formid);

	List<ChangeWhFormEntry> selectByFormid(String formid);
	
}