package com.wimoor.amazon.finances.mapper;

import com.wimoor.amazon.finances.pojo.entity.FBAReimbursementsFeeReport;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-17
 */
@Mapper
public interface FBAReimbursementsFeeReportMapper extends BaseMapper<FBAReimbursementsFeeReport> {
	void insertBatch(List<FBAReimbursementsFeeReport> itemlist);

	Page<Map<String, Object>> findByCondition(Page<Object> page, @Param("param")Map<String, Object> parameter);
	
	
	List<Map<String, Object>> findByCondition(@Param("param")Map<String, Object> parameter);

	void repaireMarket();

	List<Map<String, Object>> findSku_Reimbursements(Map<String, Object> param);
}
