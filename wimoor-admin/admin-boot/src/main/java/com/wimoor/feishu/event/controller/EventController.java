package com.wimoor.feishu.event.controller;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.sdk.servlet.ext.ServletAdapter;
import com.wimoor.feishu.event.handler.P1LeaveApprovalHandler;
import com.wimoor.feishu.pojo.entity.Auth;
import com.wimoor.feishu.service.IAuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
  @Autowired
  private ServletAdapter servletAdapter;
  @Autowired
  private IAuthService iAuthService;
  @Autowired
  P1LeaveApprovalHandler p1LeaveApprovalHandler;
  //3. 创建路由处理器
  @RequestMapping("/webhook/event/{appid}")
  public void event(@PathVariable String appid,HttpServletRequest request, HttpServletResponse response)
      throws Throwable {
	  Auth auth = iAuthService.getById("cli_"+appid);
	  EventDispatcher eventDispatcher = EventDispatcher.newBuilder(auth.getVerificationToken(), auth.getEncryptKey())
			  .onCustomizedEvent("leave_approval",p1LeaveApprovalHandler)
			  .onCustomizedEvent("approval",p1LeaveApprovalHandler)
			  .onCustomizedEvent("approval_instance",p1LeaveApprovalHandler)
			  .onCustomizedEvent("approval_task",p1LeaveApprovalHandler)
			  .onCustomizedEvent("leave_approvalV2",p1LeaveApprovalHandler)
			  .build();
	  p1LeaveApprovalHandler.setEventDispatcher(eventDispatcher);
      //3.1 回调扩展包提供的事件回调处理器
      servletAdapter.handleEvent(request, response, eventDispatcher);
  }
}