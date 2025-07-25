package com.wimoor.erp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.pojo.entity.OrderShipPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderPlanMapper extends BaseMapper<OrderPlan> {
    IPage<Map<String,Object>> findByCondition(Page<?> page, @Param("param")Map<String, Object> param);
    List<Map<String, Object>> getPlanData(@Param("param")Map<String, Object> param);
    List<Map<String, Object>> getExpandCountryData(@Param("param")Map<String, Object> param);
    List<Map<String,Object>> getPurchase(@Param("shopid")String shopid,@Param("warehouseid") String warehouseid);
    List<Map<String,Object>> getShip(String shopid);
}
