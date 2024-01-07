package com.wimoor.amazon.notifications.service.impl;

import com.wimoor.amazon.notifications.pojo.entity.AmzNotificationsDestination;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.notifications.mapper.AmzNotificationsDestinationMapper;
import com.wimoor.amazon.notifications.service.IAmzNotificationsDestinationService;
import com.amazon.spapi.api.NotificationsApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.notifications.CreateDestinationRequest;
import com.amazon.spapi.model.notifications.CreateDestinationResponse;
import com.amazon.spapi.model.notifications.DeleteDestinationResponse;
import com.amazon.spapi.model.notifications.Destination;
import com.amazon.spapi.model.notifications.DestinationList;
import com.amazon.spapi.model.notifications.DestinationResource;
import com.amazon.spapi.model.notifications.DestinationResourceSpecification;
import com.amazon.spapi.model.notifications.GetDestinationsResponse;
import com.amazon.spapi.model.notifications.SqsResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	
    @Value("${aws.sqsStandArn}")
    String sqsStandArn;
    
    String sqsname=null;
    public String getSqsName() {
    	if(sqsname!=null)return sqsname;
    	if(sqsStandArn!=null) {
    		String[] arnarray = this.sqsStandArn.split(":");
    		if(arnarray.length>0) {
    			sqsname=arnarray[arnarray.length-1];
    		}
    	}
    	return sqsname;
    }
	void handlerCreateDestinationResponse(AmazonAuthority amazonAuthority,		Destination rs ) {
		AmzNotificationsDestination newobj=new AmzNotificationsDestination();
    	newobj.setDestinationid(rs.getDestinationId());
    	newobj.setName(rs.getName());
    	newobj.setRefreshtime(new Date());
    	newobj.setAwsregion(amazonAuthority.getAWSRegion());
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
    	DestinationList result = null;
    	if(response!=null) {
          result=response.getPayload();
    	}
    	Boolean hasSQS=false;
		if(result!=null&&result.size()>0) {
			for(Destination rs:result) {
			    AmzNotificationsDestination old = this.baseMapper.selectById(rs.getDestinationId());
			    if(old!=null) { 
			    	if(old.getName().equals(getSqsName())) {
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
			    	if(needupdate) {
			    		this.baseMapper.updateById(old);
			    	}
			    }else {
			    	hasSQS=true;
					handlerCreateDestinationResponse(amazonAuthority,rs);
			    }
			}
		}
       if(!hasSQS) {
    	    NotificationsApi api = apiBuildService.getNotificationsApiGrantless(amazonAuthority);
			CreateDestinationRequest body=new CreateDestinationRequest();
			body.setName(getSqsName());
			DestinationResourceSpecification resource=new DestinationResourceSpecification();
			SqsResource sqs=new SqsResource();
			sqs.setArn(sqsStandArn);
			resource.setSqs(sqs);
			body.setResourceSpecification(resource);
			try {
				CreateDestinationResponse createResponse = api.createDestination(body);
				Destination rs = createResponse.getPayload();
				handlerCreateDestinationResponse(amazonAuthority,rs);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    public void delete(AmzNotificationsDestination item) {
    	List<AmazonAuthority> amazonAuthorityList = amazonAuthorityService.getAllAuth();
    	for(AmazonAuthority amazonAuthority:amazonAuthorityList) {
    		 if(amazonAuthority.getAWSRegion().equals(item.getAwsregion())) {
    			 NotificationsApi api = apiBuildService.getNotificationsApiGrantless(amazonAuthority);
	     			try {
	     					DeleteDestinationResponse response = api.deleteDestination(item.getDestinationid());
	     					if(response.getErrors().isEmpty()) {
	     						this.baseMapper.deleteById(item.getDestinationid());
	     						return;
	     					}
	     			 
	     			}catch(ApiException e) {
	     				System.out.println(e.getResponseBody());
	     				e.printStackTrace();
	     			}
    		 }
    		
    	}
	   
    }
    
    public void deleteNotificationsDestination(AmazonAuthority amazonAuthority) {
	    QueryWrapper<AmzNotificationsDestination> queryWrapper=new QueryWrapper<AmzNotificationsDestination>();
	    queryWrapper.eq("name",getSqsName());
		List<AmzNotificationsDestination> list = this.baseMapper.selectList(queryWrapper);
		for(AmzNotificationsDestination item:list) {
			delete(item);
		}
    }
    
	@Override
	public void executTask() {
		  List<AmazonAuthority> amazonAuthorityList = amazonAuthorityService.getAllAuth();
		  Set<String> region=new HashSet<String>();
		  for(AmazonAuthority amazonAuthority : amazonAuthorityList) {
			    if(region.contains(amazonAuthority.getAWSRegion())) {
			    	continue;
			    }
			    QueryWrapper<AmzNotificationsDestination> queryWrapper=new QueryWrapper<AmzNotificationsDestination>();
			    queryWrapper.eq("name",getSqsName());
			    queryWrapper.eq("awsregion",amazonAuthority.getAWSRegion());
				List<AmzNotificationsDestination> list = this.baseMapper.selectList(queryWrapper);
			    if(list!=null&&list.size()>0) {
			    	  region.add(amazonAuthority.getAWSRegion());
			    	  continue;
			    }
			    NotificationsApi api = apiBuildService.getNotificationsApiGrantless(amazonAuthority);
				   try {
						GetDestinationsResponse response = api.getDestinations();
						handlerGetDestinationsResponse(amazonAuthority,response);
					} catch (ApiException e) {
						// TODO Auto-generated catch block
					   e.printStackTrace();
				   }
				  
		  }
		
	}
	@Override
	public void deleteDestination(String id) {
		// TODO Auto-generated method stub
		 List<AmazonAuthority> list = amazonAuthorityService.getAllAuth();
		 NotificationsApi api = apiBuildService.getNotificationsApiGrantless(list.get(0));
		 DeleteDestinationResponse response;
		try {
			response = api.deleteDestination(id);
			System.out.println(response.toString());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
