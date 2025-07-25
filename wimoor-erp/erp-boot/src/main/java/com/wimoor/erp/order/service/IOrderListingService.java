package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.order.pojo.entity.OrderListing;

import java.util.Map;

public interface IOrderListingService extends IService<OrderListing> {
    Map<String,Object> summary(String companyid);
}
