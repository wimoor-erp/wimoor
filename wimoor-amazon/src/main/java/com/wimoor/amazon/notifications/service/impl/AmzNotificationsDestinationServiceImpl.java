package com.wimoor.amazon.notifications.service.impl;

import com.wimoor.amazon.notifications.pojo.entity.AmzNotifications;
import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsDestination;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.notifications.mapper.AmzNotificationsDestinationMapper;
import com.wimoor.amazon.notifications.service.IAmzNotificationsDestinationService;
import com.amazon.spapi.api.NotificationsApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.notifications.CreateDestinationRequest;
import com.amazon.spapi.model.notifications.CreateDestinationResponse;
import com.amazon.spapi.model.notifications.Destination;
import com.amazon.spapi.model.notifications.DestinationList;
import com.amazon.spapi.model.notifications.DestinationResource;
import com.amazon.spapi.model.notifications.DestinationResourceSpecification;
import com.amazon.spapi.model.notifications.GetDestinationsResponse;
import com.amazon.spapi.model.notifications.SqsResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 亚马逊Destination 亚马逊消息接受对象 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-27
 */
@Service
public class AmzNotificationsDestinationServiceImpl extends ServiceImpl<AmzNotificationsDestinationMapper, AmzNotificationsDestination> implements IAmzNotificationsDestinationService {

	@Autowired
	ApiBuildService apiBuildService;
	
    @Value("${aws.sqsStandArn}")
    String sqsStandArn;
    
	void handlerCreateDestinationResponse(AmazonAuthority amazonAuthority,	CreateDestinationResponse createResponse) {
		Destination rs = createResponse.getPayload();
		AmzNotificationsDestination newobj=new AmzNotificationsDestination();
    	newobj.setDestinationid(rs.getDestinationId());
    	newobj.setName(rs.getName());
    	newobj.setRefreshtime(new Date());
    	newobj.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
    	DestinationResource myresource = rs.getResource();
    	if(myresource.getSqs()!=null&&myresource.getSqs().getArn()!=null) {
    		newobj.setResourceSqsArn(myresource.getSqs().getArn());
    	}
    	if(myresource.getEventBridge()!=null&&myresource.getEventBridge().getAccountId()!=null) {
    		newobj.setResourceEventBridgeAccountid(myresource.getEventBridge().getAccountId());
    		newobj.setResourceEventBridgeRegion(myresource.getEventBridge().getRegion());
    		newobj.setResourceEventBridgeName(myresource.getEventBridge().getName());
    	}
    	this.baseMapper.insert(newobj);
	}
	
    void handlerGetDestinationsResponse(AmazonAuthority amazonAuthority,GetDestinationsResponse response){
    	DestinationList result = response.getPayload();
    	Boolean hasSQS=false;
		if(result!=null&&result.size()>0) {
			for(Destination rs:result) {
			    AmzNotificationsDestination old = this.baseMapper.selectById(rs.getDestinationId());
			    if(old!=null) { 
			    	if(old.getName().equals(AmzNotifications.sqsname)) {
						hasSQS=true;
					}
			    	boolean needupdate = false;
			    	if(old.getName()==null||!old.getName().equals(rs.getName())) {
			    		old.setName(rs.getName());
			    		needupdate=true;
			    	}
			    	old.setRefreshtime(new Date());
			    	DestinationResource resource = rs.getResource();
			    	
			    	if(resource.getSqs()!=null&&rs.getResource().getSqs().getArn()!=null) {
			    		String arn = rs.getResource().getSqs().getArn();
			    		if(old.getResourceSqsArn()==null||!old.getResourceSqsArn().equals(arn)) {
			    			old.setResourceSqsArn(arn);
			    			needupdate=true;
			    		}
			    	}
			    	if(resource.getEventBridge()!=null) {
			    		if(resource.getEventBridge().getAccountId()!=null) {
			    			if(old.getResourceEventBridgeAccountid()==null||!old.getResourceEventBridgeAccountid().equals(resource.getEventBridge().getAccountId())) {
					    		old.setResourceEventBridgeAccountid(resource.getEventBridge().getAccountId());
					    		needupdate=true;
				    		}
			    		}
			    	    if(resource.getEventBridge().getRegion()!=null) {
			    	    	   if(old.getResourceEventBridgeRegion()==null||!old.getResourceEventBridgeRegion().equals(resource.getEventBridge().getRegion())) {
						    		old.setResourceEventBridgeRegion(resource.getEventBridge().getRegion());
						    		needupdate=true;
				                }
			    	    }
		                if(resource.getEventBridge().getName()!=null) {
		                	 if(old.getResourceEventBridgeName()==null||old.getResourceEventBridgeName().equals(resource.getEventBridge().getName())) {
                                	old.setResourceEventBridgeName(resource.getEventBridge().getName());
                                	needupdate=true;
                                }
		                }
			    	}
			    	if(amazonAuthority.getId()!=null) {
			    		if(old.getAmazonauthid()==null||!old.getAmazonauthid().toString().equals(amazonAuthority.getId())) {
			    			old.setAmazonauthid(new BigInteger(amazonAuthority.getId()));
			    			needupdate=true;
			    		}
			    	}
			    	if(needupdate) {
			    		this.baseMapper.updateById(old);
			    	}
			    }
			}
		}
       if(!hasSQS) {
    	    NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
			CreateDestinationRequest body=new CreateDestinationRequest();
			body.setName(AmzNotifications.sqsname);
			DestinationResourceSpecification resource=new DestinationResourceSpecification();
			SqsResource sqs=new SqsResource();
			sqs.setArn(sqsStandArn);
			resource.setSqs(sqs);
			body.setResourceSpecification(resource);
			try {
				CreateDestinationResponse createResponse = api.createDestination(body);
				handlerCreateDestinationResponse(amazonAuthority,createResponse);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		  NotificationsApi api = apiBuildService.getNotificationsApi(amazonAuthority);
		   try {
				GetDestinationsResponse response = api.getDestinations();
				handlerGetDestinationsResponse(amazonAuthority,response);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
