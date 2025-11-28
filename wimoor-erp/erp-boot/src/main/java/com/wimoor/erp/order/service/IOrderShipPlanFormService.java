package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.order.pojo.entity.OrderShipPlanForm;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public interface IOrderShipPlanFormService extends IService<OrderShipPlanForm> {

    IPage<Map<String,Object>> findPlanFrom(Page<Object> page, Map<String, String> param);

    Workbook downloadOrderPlanForm(UserInfo user,String id);

    void outEntry(OrderShipPlanForm form);
}
