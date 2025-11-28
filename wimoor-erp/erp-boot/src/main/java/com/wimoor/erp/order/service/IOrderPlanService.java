package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.order.pojo.entity.OrderPlan;

import java.util.List;
import java.util.Map;

public interface IOrderPlanService extends IService<OrderPlan> {
    IPage<Map<String,Object>> findByCondition(PlanDTO dto);

    List<Map<String,Object>> getExpandCountryData(Map<String,Object>  param);

    Object bindMSku(Map<String, Object> param);

    Object refreshData(Map<String, Object> param);

    Object clear(Map<String, Object> param);

    Object getPurchase(UserInfo user, String warehouseid);

    Object getShip(String companyid);
}
