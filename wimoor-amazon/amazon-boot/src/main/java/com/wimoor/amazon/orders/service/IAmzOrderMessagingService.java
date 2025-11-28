package com.wimoor.amazon.orders.service;

import com.amazon.spapi.model.messaging.GetMessagingActionsForOrderResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;

public interface IAmzOrderMessagingService {
    public GetMessagingActionsForOrderResponse getMessagingActionsForOrder(AmazonAuthority auth , String amazonOrderId, String marketplaceid);
}
