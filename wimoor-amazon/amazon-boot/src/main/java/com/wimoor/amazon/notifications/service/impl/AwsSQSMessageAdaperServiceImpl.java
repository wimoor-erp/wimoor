package com.wimoor.amazon.notifications.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.notifications.service.IAwsSQSMessageAdaperService;
import com.wimoor.amazon.notifications.service.IAwsSQSMessageHandlerService;
import com.wimoor.common.GeneralUtil;

@Service
public class AwsSQSMessageAdaperServiceImpl implements IAwsSQSMessageAdaperService{

	@Resource
	IAwsSQSMessageHandlerService productFollowHandlerService;
	@Resource
	IAwsSQSMessageHandlerService amazonOrdersService;
	@Resource
	IAwsSQSMessageHandlerService submitfeedService;
	@Resource
	IAwsSQSMessageHandlerService reportHandlerService;
	private void handlerMsg(IAwsSQSMessageHandlerService service,JSONObject payload){
		 new Thread(new Runnable() {
				@Override
				public void run() {
					service.handlerMessage(payload);
				 }
		 }).start();
	}
	
	public boolean handlerMessage(String body) {
		// TODO Auto-generated method stub
		if(body==null||body.contains("<Notification>")) {
			return true;
		}else {
					// TODO Auto-generated method stub
			      try {
			    	  JSONObject notificationVersion = GeneralUtil.getJsonObject(body);
			    	     if(notificationVersion==null) {
			    	    	 return true;
			    	     }
						 String notificationType= notificationVersion.getString("NotificationType");
						 
						 if(notificationType==null) {
							 notificationType = notificationVersion.getString("notificationType");
							 if(notificationType==null) {
								 return true;
							 }
						 }
						 if(notificationType.equals("ANY_OFFER_CHANGED")) {
							 handlerMsg(productFollowHandlerService,notificationVersion.getJSONObject("Payload"));
						 }else if(notificationType.equals("ORDER_STATUS_CHANGE")) {
							 handlerMsg(amazonOrdersService,notificationVersion.getJSONObject("Payload"));
						 }else if(notificationType.equals("FEED_PROCESSING_FINISHED")){
							 handlerMsg(submitfeedService,notificationVersion.getJSONObject("payload"));
						 }else if(notificationType.equals("REPORT_PROCESSING_FINISHED")){
							 handlerMsg(reportHandlerService,notificationVersion.getJSONObject("payload"));
						 }
			      }catch(Exception e) {
			    	  e.printStackTrace();
			    	  return false;
			      }
			 return true;
		}
		
	}

 
 
}
