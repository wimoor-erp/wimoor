package com.wimoor.amazon.orders.service.impl;

import com.amazon.spapi.SellingPartnerAPIAA.LWAException;
import com.amazon.spapi.api.MessagingApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.messaging.GetMessagingActionsForOrderResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.orders.service.IAmzOrderMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Service("amzOrderMessagingService")
public class AmzOrderMessagingServiceImpl implements IAmzOrderMessagingService {
    @Resource
    IAmazonAuthorityService amazonAuthorityService;

    @Resource
    IAmzAuthApiTimelimitService iAmzAuthApiTimelimitService;

    @Resource
    IMarketplaceService marketplaceService;

    @Autowired
    ApiBuildService apiBuildService;
    public GetMessagingActionsForOrderResponse getMessagingActionsForOrder(AmazonAuthority auth ,String amazonOrderId,String marketplaceid){
        MessagingApi api = apiBuildService.getMessageApi(auth);
        try {
            GetMessagingActionsForOrderResponse response = api.getMessagingActionsForOrder(amazonOrderId, Arrays.asList(marketplaceid));
            return response;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (LWAException e) {
            throw new RuntimeException(e);
        }
    }


}
