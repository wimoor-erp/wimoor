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
import com.amazon.spapi.model.notifications.DeleteSubscriptionByIdResponse;
import com.amazon.spapi.model.notifications.GetSubscriptionResponse;
import com.amazon.spapi.model.notifications.Subscription;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
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
	@Autowired
	IAmazonAuthorityService iAmazonAuthorityService;
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
			QueryWrapper<AmzNotificationsSubscriptions> query=new QueryWrapper<AmzNotificationsSubscriptions>();
			query.eq("subscriptionId", subscriptionId);
			query.eq("amazonauthid", amazonAuthority.getId());
			query.eq("eventFilterType", type.getNotifications());
			AmzNotificationsSubscriptions oldone = this.baseMapper.selectOne(query);
			if(oldone==null) {
				this.baseMapper.insert(subpojo);
			}else {
				this.baseMapper.update(subpojo, query);
			}
		}
	}
	
	void requestCreateSubscription(AmazonAuthority amazonAuthority,AmzNotifications type) throws ApiException {
		amazonAuthority.setUseApi("createSubscription");
		NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
		List<AmzNotificationsDestination> destinationList = iAmzNotificationsDestinationService.list(new LambdaQueryWrapper<AmzNotificationsDestination>()
				.eq(AmzNotificationsDestination::getAwsregion, amazonAuthority.getAWSRegion())
				.eq(AmzNotificationsDestination::getName, iAmzNotificationsDestinationService.getSqsName()));
		if(destinationList==null||destinationList.size()==0)return;
		AmzNotificationsDestination destination=destinationList.get(0);
		CreateSubscriptionRequest body=new CreateSubscriptionRequest();
		body.setDestinationId(destination.getDestinationid());
		body.setPayloadVersion("1.0");
		try {
			CreateSubscriptionResponse createResponse=api.createSubscription(body, type.getNotifications());
			handlerCreateSubscriptionResponse(amazonAuthority,type,createResponse);
		}catch(ApiException e) {
			System.out.println(e.getResponseBody());
			e.printStackTrace();
		}
		
	}
	
	void handlerGetSubscriptionResponse(AmazonAuthority amazonAuthority,AmzNotifications type,GetSubscriptionResponse response){
		boolean hasSubcritpion=false;
	
		if(response!=null&&response.getPayload()!=null) {
			hasSubcritpion=true;
			Subscription subscription = response.getPayload();
			String subscriptionId=subscription.getSubscriptionId();
			QueryWrapper<AmzNotificationsSubscriptions> query=new QueryWrapper<AmzNotificationsSubscriptions>();
			query.eq("subscriptionId", subscriptionId);
			query.eq("amazonauthid", amazonAuthority.getId());
			String eventFilterType="#";
			if(subscription.getProcessingDirective()!=null&&subscription.getProcessingDirective().getEventFilter()!=null) {
				eventFilterType= subscription.getProcessingDirective().getEventFilter().getEventFilterType();
			}
		    query.eq("eventFilterType",eventFilterType);
			AmzNotificationsSubscriptions oldone = this.baseMapper.selectOne(query);
				if(oldone==null) {
					AmzNotificationsSubscriptions one =new AmzNotificationsSubscriptions();
					one.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
					one.setSubscriptionId(subscriptionId);
					one.setDestinationId(subscription.getDestinationId());
					one.setEventFilterType(eventFilterType);
					one.setPayloadVersion(subscription.getPayloadVersion());
					one.setRefreshtime(LocalDateTime.now());
					this.baseMapper.insert(one);
				}
			} 
		try {
			if(!hasSubcritpion) {
				requestCreateSubscription(amazonAuthority,type);
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
public void deleteSubscriptions(String destinationId) {
	LambdaQueryWrapper<AmzNotificationsSubscriptions> query=new LambdaQueryWrapper<AmzNotificationsSubscriptions>();
	query.eq(AmzNotificationsSubscriptions::getDestinationId, destinationId);
    List<AmzNotificationsSubscriptions> list = this.baseMapper.selectList(query);
    for(AmzNotificationsSubscriptions item:list) {
    	AmazonAuthority amazonAuthority =iAmazonAuthorityService.getById(item.getAmazonauthid().toString()) ;
    	NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
    	try {
			DeleteSubscriptionByIdResponse response = api.deleteSubscriptionById(item.getSubscriptionId(),item.getEventFilterType());
		     System.out.println(response.getErrors().get(0));
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	}

	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		List<AmzNotifications> typeList = iAmzNotificationsService.list(new LambdaQueryWrapper<AmzNotifications>().eq(AmzNotifications::getIsrun, Boolean.TRUE));
		amazonAuthority.setUseApi("getSubscription");
		NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
		for(AmzNotifications type:typeList) {
			try {
				QueryWrapper<AmzNotificationsSubscriptions> query=new QueryWrapper<AmzNotificationsSubscriptions>();
				query.eq("amazonauthid", amazonAuthority.getId());
				query.eq("eventFilterType",type.getNotifications());
				List<AmzNotificationsSubscriptions> oldList = this.baseMapper.selectList(query);
				if(oldList!=null&&oldList.size()>0) {
					continue;
				}
				GetSubscriptionResponse response = api.getSubscription(type.getNotifications());
				handlerGetSubscriptionResponse(amazonAuthority,type,response);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
					try {
						requestCreateSubscription(amazonAuthority,type);
					} catch (ApiException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 
			}
		}
		
	}

}
