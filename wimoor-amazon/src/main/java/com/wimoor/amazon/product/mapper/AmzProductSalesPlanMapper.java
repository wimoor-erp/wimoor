package com.wimoor.amazon.product.mapper;

import com.wimoor.amazon.product.pojo.dto.ShipPlanDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductSalesPlan;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
	List<Map<String, Object>> getShipPlanModel(ShipPlanDTO dto);
	Map<String, Object> filterMsku(ShipPlanDTO dto);
	List<Map<String, Object>> ExpandCountryDataByGroup(String shopid, String groupid,String warehouseid ,String msku);
	List<Map<String, Object>> ExpandEUCountryDataByGroup(String shopid, String groupid,String warehouseid ,String msku);
}
