package com.wimoor.feishu.event.handler;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lark.oapi.Client;
import com.lark.oapi.core.request.EventReq;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.event.CustomEventHandler;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.service.im.v1.enums.MsgTypeEnum;
import com.lark.oapi.service.im.v1.model.ext.MessageText;
import com.wimoor.feishu.config.FeiShuClientBuilder;
import com.wimoor.feishu.service.ILeaveCalendarService;
@Component
public class P1LeaveApprovalHandler extends CustomEventHandler {

	EventDispatcher eventDispatcher=null;
    @Autowired 
    ILeaveCalendarService iLeaveCalendarService;
    @Autowired 
    FeiShuClientBuilder clientBuilder;
	 
	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		// TODO Auto-generated constructor stub
		this.eventDispatcher=eventDispatcher;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handle(EventReq event) throws Exception {
		// TODO Auto-generated method stub
		String cipherEventJsonStr = eventDispatcher.parseReq(event);
        // 解密请求，如果需要的话
        String plainEventJsonStr =  eventDispatcher.decryptEvent(cipherEventJsonStr);
        // 解析关键字段
        Map<String,Object> json = Jsons.DEFAULT.fromJson(plainEventJsonStr,Map.class);
        Map<String,Object> eventmap=json.get("event")!=null?(Map<String, Object>) json.get("event"):null;
        if(eventmap!=null) {
        	String appid=eventmap.get("app_id")!=null?eventmap.get("app_id").toString():null;
        	String type=eventmap.get("type")!=null?eventmap.get("type").toString():null;
        	String  employee_id=eventmap.get("employee_id")!=null?eventmap.get("employee_id").toString():null;
          	//String  tenantKey=eventmap.get("tenant_key")!=null?eventmap.get("tenant_key").toString():null;
          	//String  open_id=eventmap.get("open_id")!=null?eventmap.get("open_id").toString():null;
            // 验签逻辑
            if(appid!=null&&type!=null&&employee_id!=null) {
            	if(type.equals("leave_approval")) {
                	try {
                        // 发送请求
                		Client client = clientBuilder.getClient(appid);
                		 Map<String, Object> body = new HashMap<>();
                		    body.put("receive_id", employee_id);
                		    body.put("content", MessageText.newBuilder()
                		        .atUser(employee_id, "深圳市科方达科技有限公司")
                		        .text("审核已处理，并自动为您创建请假日程，感谢您的辛苦工作")
                		        .build());
                		    body.put("msg_type", MsgTypeEnum.MSG_TYPE_TEXT.getValue());

                		    // 发起请求
                		    RawResponse resp = client.post(
                		        "https://open.feishu.cn/open-apis/im/v1/messages?receive_id_type=user_id"
                		        , body
                		        , AccessTokenType.Tenant);
                		 // 处理结果
                		    System.out.println(resp.getStatusCode());
                		    System.out.println(Jsons.DEFAULT.toJson(resp.getHeaders()));
                		    System.out.println(new String(resp.getBody()));
                		    System.out.println(resp.getRequestID());
                		    
                		String instance_code=eventmap.get("instance_code").toString();
                		String leave_start_time=eventmap.get("leave_start_time").toString();
                		String leave_end_time=eventmap.get("leave_end_time").toString();
                		iLeaveCalendarService.addLeaveCalandar(appid,instance_code,employee_id,leave_start_time ,leave_end_time,plainEventJsonStr);
                	}catch(Exception e) {
                		e.printStackTrace(); 
                	}
                }
                if(type.equals("leave_approval_revert")) {
                	String instance_code=eventmap.get("instance_code").toString();
                	iLeaveCalendarService.removeLeaveCalandar(appid,instance_code);
                }
            }
            
        }
        
	}

}
