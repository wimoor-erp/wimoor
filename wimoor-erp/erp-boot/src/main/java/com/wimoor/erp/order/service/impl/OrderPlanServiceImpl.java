package com.wimoor.erp.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.order.mapper.OrderPlanMapper;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.service.IOrderPlanService;
import org.springframework.stereotype.Service;

@Service
public class OrderPlanServiceImpl extends ServiceImpl<OrderPlanMapper, OrderPlan> implements IOrderPlanService {

}
