package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.dto.PlanDTO;
import com.wimoor.amazon.product.pojo.dto.PlanDetailDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlan;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Mapper
public interface AmzProductSalesPlanMapper extends BaseMapper<AmzProductSalesPlan> {
	List<Map<String, Object>> getPlanModel(@Param("dto")PlanDTO dto);
	IPage<Map<String, Object>> getPlanModel(Page<?> page,@Param("dto") PlanDTO dto);
	Map<String, Object> filterMsku(PlanDTO dto);
	List<Map<String, Object>> ExpandCountryDataByGroup(PlanDetailDTO dto);
	List<Map<String, Object>> ExpandEUCountryDataByGroup(PlanDetailDTO dto);

}
