package com.wimoor.erp.inventory.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.erp.inventory.pojo.entity.StockTaking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface StockTakingMapper extends BaseMapper<StockTaking> {

	List<Map<String, Object>> findByCondition(Map<String, Object> map);

	Map<String, BigDecimal> getTotalProfitAndLoss(String id);

	List<Map<String, Object>> getItemList(@Param("id") String id, @Param("warehouseid") String warehouseid,@Param("materialid") String materialid);

	Map<String, Object> getSumQuantity(@Param("id") String id, @Param("warehouseid") String warehouseid);

	List<Map<String, Object>> findStockInvByParentId(@Param("warehouseid")String wid);

}