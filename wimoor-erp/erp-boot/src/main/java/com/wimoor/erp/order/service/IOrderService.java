package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.order.pojo.entity.Order;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

public interface IOrderService extends IService<Order> {
    IPage<Map<String,Object>> findByCondition(BasePageQuery dto);
    public IPage<Map<String, Object>> findOrderByCondition(BasePageQuery dto);
    public List<Map<String, Object>> listOrderByCondition(BasePageQuery dto);
    IPage<Map<String, Object>> findMaterialByCondition(Page<?> page, Map<String, String> map);
    List<Map<String, Object>> findMaterialBySelect(Map<String, String> map);
    String uploadOrder(Sheet sheet, UserInfo user);
}
