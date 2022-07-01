package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface PurchasePlanMapper extends BaseMapper<PurchasePlan> {

	IPage<Map<String, Object>> getSale(Page<?> page,Map<String,Object> map);
	
	List<Map<String, Object>> getPurchasePlanForShopId(@Param("shopid") String shopid);
	
	List<Map<String, Object>> getNotPlanWarehouse(@Param("shopid") String shopid);
	
	List<Map<String, Object>> getUsePlanWarehouse(@Param("shopid") String shopid);
}