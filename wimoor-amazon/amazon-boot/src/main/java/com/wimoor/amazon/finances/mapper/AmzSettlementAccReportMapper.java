package com.wimoor.amazon.finances.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.finances.pojo.entity.AmzSettlementAccReport;
 
@Mapper
public interface AmzSettlementAccReportMapper extends BaseMapper<AmzSettlementAccReport>{
	Page<Map<String, Object>> findSettlementAcc(IPage<?> page,@Param("param") Map<String, Object> map);
	List<Map<String, Object>> findSettlementAcc(@Param("param") Map<String, Object> map);
	List<Map<String, Object>> findSettlementAccSum(Map<String, Object> map);
	List<Map<String,Object>> sumSettlementAcc(Map<String, Object> map);
	List<Map<String, Object>> findDateByAuth(Map<String, Object> map);
	List<AmzSettlementAccReport> findlist(Map<String,Object> param);
}