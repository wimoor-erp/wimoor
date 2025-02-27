package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.order.pojo.entity.OrderPlanForm;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import java.util.Map;

public interface IOrderPlanFormService extends IService<OrderPlanForm> {

    IPage<Map<String,Object>> findPlanFrom(Page<Object> page, Map<String, String> param);

    Workbook downloadOrderPlanForm(UserInfo user,String id);
}
