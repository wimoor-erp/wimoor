package com.wimoor.amazon.notifications.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.NotificationsApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.notifications.CreateSubscriptionRequest;
import com.amazon.spapi.model.notifications.CreateSubscriptionResponse;
import com.amazon.spapi.model.notifications.GetSubscriptionResponse;
import com.amazon.spapi.model.notifications.Subscription;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.notifications.mapper.AmzNotificationsSubscriptionsMapper;
import com.wimoor.amazon.notifications.pojo.entity.AmzNotifications;
import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsDestination;
import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsSubscriptions;
import com.wimoor.amazon.notifications.service.IAmzNotificationsDestinationService;
import com.wimoor.amazon.notifications.service.IAmzNotificationsService;
import com.wimoor.amazon.notifications.service.IAmzNotificationsSubscriptionsService;

/**
 * <p>
 * 订阅消息对象 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@Service
public class AmzNotificationsSubscriptionsServiceImpl extends ServiceImpl<AmzNotificationsSubscriptionsMapper, AmzNotificationsSubscriptions> implements IAmzNotificationsSubscriptionsService {
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IAmzNotificationsService iAmzNotificationsService;
	@Autowired
	IAmzNotificationsDestinationService iAmzNotificationsDestinationService;
	
	void handlerCreateSubscriptionResponse(AmazonAuthority amazonAuthority,AmzNotifications type,CreateSubscriptionResponse createResponse) {
		if(createResponse!=null&&createResponse.getPayload()!=null) {
			Subscription subscription = createResponse.getPayload();
			String subscriptionId=subscription.getDestinationId();
			String destinationId=subscription.getDestinationId();
			String payloadVersion=subscription.getPayloadVersion();
			AmzNotificationsSubscriptions subpojo=new AmzNotificationsSubscriptions();
			subpojo.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
			subpojo.setSubscriptionId(subscriptionId);
			subpojo.setDestinationId(destinationId);
			subpojo.setPayloadVersion(payloadVersion);
			subpojo.setEventFilterType(type.getNotifications());
			subpojo.setRefreshtime(LocalDateTime.now());
			this.baseMapper.insert(subpojo);
		}
	}
	
	void handlerGetSubscriptionResponse(AmazonAuthority amazonAuthority,AmzNotifications type,GetSubscriptionResponse response){
		boolean hasSubcritpion=false;
	
		if(response!=null&&response.getPayload()!=null) {
			Subscription subscription = response.getPayload();
			String subscriptionId=subscription.getSubscriptionId();
			AmzNotificationsSubscriptions oldone = this.baseMapper.selectById(subscriptionId);
			if(oldone==null) {
				hasSubcritpion=true;
			}
		}
		try {
			if(!hasSubcritpion) {
				NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
				AmzNotificationsDestination destination = iAmzNotificationsDestinationService.getOne(new LambdaQueryWrapper<AmzNotificationsDestination>()
						.eq(AmzNotificationsDestination::getAmazonauthid, amazonAuthority.getId())
						.eq(AmzNotificationsDestination::getName, AmzNotifications.sqsname));
				CreateSubscriptionRequest body=new CreateSubscriptionRequest();
				body.setDestinationId(destination.getDestinationid());
				body.setPayloadVersion("1.0");
				CreateSubscriptionResponse createResponse=api.createSubscription(body, type.getNotifications());
				handlerCreateSubscriptionResponse(amazonAuthority,type,createResponse);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<AmzNotifications> typeList = iAmzNotificationsService.list(new LambdaQueryWrapper<AmzNotifications>().eq(AmzNotifications::getIsrun, Boolean.TRUE));
		NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
		for(AmzNotifications type:typeList) {
			try {
				GetSubscriptionResponse response = api.getSubscription(type.getNotifications());
				handlerGetSubscriptionResponse(amazonAuthority,type,response);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
