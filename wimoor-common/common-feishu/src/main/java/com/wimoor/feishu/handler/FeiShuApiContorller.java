package com.wimoor.feishu.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.mybatisplus.MysqlGenerator;
import com.wimoor.common.result.Result;
import com.wimoor.feishu.service.ILeaveCalendarService;

@RestController
public class FeiShuApiContorller {
	@Autowired
	ILeaveCalendarService iLeaveCalendarService;
	  //3. 创建路由处理器
	  @GetMapping("/feishu/calendar")
	  public void calendar(){
	    //3.1 回调扩展包提供的事件回调处理器 cdf7eef8
		  //iUserCalandarService.addLeaveCalandar("cli_a2e0b35e2462d00b","123456789","cdf7eef8", "2023-09-02", "2023-09-04");
		  //iLeaveCalendarService.deleteLeaveCalandar("cli_a2e0b35e2462d00b","123456789");
		  iLeaveCalendarService.checkCalandar();
	  }
	  
		 @GetMapping("/feishu/createpojo")
		 public Result<String> createPojoAction(String table,String pkg) {
		    	MysqlGenerator.autoGenerator(table,pkg, "t_sys_feishu_");
		        return Result.success("true");
		 }
}
