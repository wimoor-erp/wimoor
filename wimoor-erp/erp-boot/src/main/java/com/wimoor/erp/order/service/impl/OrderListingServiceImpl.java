package com.wimoor.erp.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.order.mapper.OrderListingMapper;
import com.wimoor.erp.order.pojo.entity.OrderListing;
import com.wimoor.erp.order.service.IOrderListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderListingServiceImpl extends ServiceImpl<OrderListingMapper, OrderListing> implements IOrderListingService {

    @Override
    public Map<String, Object> summary( String shopid) {
        return this.baseMapper.summary(shopid);
    }
}
