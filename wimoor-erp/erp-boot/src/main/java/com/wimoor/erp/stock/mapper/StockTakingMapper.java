package com.wimoor.erp.stock.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.stock.pojo.entity.StockTaking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface StockTakingMapper extends BaseMapper<StockTaking> {

	IPage<Map<String, Object>> findByCondition(Page<?>  page,@Param("params")Map<String,Object> map);

	Map<String, BigDecimal> getTotalProfitAndLoss(String id);

	Map<String, Object> getSumQuantity(@Param("id") String id, @Param("warehouseid") String warehouseid);

	List<Map<String, Object>> findStockInv(@Param("warehouseid")String wid);

}