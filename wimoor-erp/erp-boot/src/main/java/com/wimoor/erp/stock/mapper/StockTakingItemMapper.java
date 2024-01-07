package com.wimoor.erp.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.stock.pojo.entity.StockTakingItem;
@Mapper
public interface StockTakingItemMapper extends BaseMapper<StockTakingItem>{
	
    List<Map<String, Object>> findLocalInventory(@Param("param")Map<String,Object> param);
	IPage<Map<String, Object>> findLocalInventory(Page<?>  page,@Param("param")Map<String, Object> param);
	List<Map<String, Object>> getItemList(@Param("id") String id, @Param("warehouseid") String warehouseid,@Param("search") String search);

}