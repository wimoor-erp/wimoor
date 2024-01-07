package com.wimoor.admin.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.admin.pojo.dto.NotifyDTO;
import com.wimoor.admin.pojo.entity.NotifyType;
import com.wimoor.admin.pojo.entity.SystemNotify;
import com.wimoor.admin.pojo.entity.SystemSubscription;
import com.wimoor.admin.pojo.entity.SystemTarget;
import com.wimoor.admin.service.INotifyHandlerService;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 

 
@Api(tags = "通知接口")
@RestController
@RequestMapping("/api/v1/notify")
@RequiredArgsConstructor
@Slf4j
public class NotifyController  {
	@Resource
	INotifyHandlerService notifyHandlerService;
 
 
	 

	@PostMapping("/addAnnounce")
	public Result<?> addAnnounceAction(@RequestBody	SystemNotify notify ) {
	    notifyHandlerService.createAnnounce(notify);
		return Result.success();
	}

	@GetMapping("/subscribe")
	public Map<String, Object> setSubscribeAction(String target,String action, String disable) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = UserInfoContext.get();
		Boolean isdisable = true;
		if ("true".equals(disable)) {
			isdisable = true;
		} else {
			isdisable = false;
		}
		Integer result = notifyHandlerService.subscribe(userInfo.getId(), target, action, isdisable);
		if (result > 0) {
			map.put("isSuccess", "true");
			map.put("msg", "操作成功！");
		} else {
			map.put("isSuccess", "false");
			map.put("msg", "操作失败！");
		}
		return map;
	}

	// 拉取公告
	@PostMapping("/getAnnounce")
	public Result<IPage<Map<String, Object>>> getAnnounceAction(@RequestBody BasePageQuery dto) {
		UserInfo userInfo = UserInfoContext.get();
		IPage<Map<String, Object>> list = notifyHandlerService.getUserNotify(dto.getPage(),userInfo, NotifyType.Announce, null);
		List<String> ids = new LinkedList<String>();
		for (Map<String, Object> map : list.getRecords()) {
			ids.add(map.get("id").toString()) ;
		}
		notifyHandlerService.read(userInfo, ids);
		Integer num = notifyHandlerService.findNoReadNumberByUser(userInfo.getId());
		if(list != null &&  list.getRecords().size() > 0) {
			 list.getRecords().get(0).put("msgcount", num);
		}
		return Result.success(list);
	}
	

	// 拉取提醒
	@PostMapping("/getRemind")
	public Result<IPage<Map<String, Object>>> getRemainAction(@RequestBody NotifyDTO dto) {
		UserInfo userInfo = UserInfoContext.get();
		String ftype =dto.getTargetType();
		if (StrUtil.isEmpty(ftype)) {
			ftype = null;
		}
		IPage<Map<String, Object>> list = notifyHandlerService.getUserNotify(dto.getPage(),userInfo, NotifyType.Remind, ftype);
		List<String> ids = new LinkedList<String>();
		for (Map<String, Object> map : list.getRecords()) {
			ids.add(map.get("id").toString()) ;
		}
		notifyHandlerService.read(userInfo, ids);
		Integer num = notifyHandlerService.findNoReadNumberByUser(userInfo.getId());
		if(list != null && list.getRecords().size() > 0) {
				list.getRecords().get(0).put("msgcount", num);
		}
		return Result.success(list);
	}

	// 获取私信消息
	@PostMapping("/getMessage")
	public  Result<IPage<Map<String, Object>>> getUserMessageAction(@RequestBody BasePageQuery dto) {
		UserInfo userInfo = UserInfoContext.get();
		IPage<Map<String, Object>> list = notifyHandlerService.getUserNotify(dto.getPage(),userInfo, NotifyType.Message, null);
		List<String> ids = new LinkedList<String>();
		for (Map<String, Object> map : list.getRecords()) {
			ids.add(map.get("id").toString()) ;
		}
		notifyHandlerService.read(userInfo, ids);
		List<SystemNotify> allmsglist = notifyHandlerService.findNoReadByUser(userInfo.getId(),null);
		if(list != null && list.getRecords().size() > 0) {
			if(allmsglist == null) {
				list.getRecords().get(0).put("msgcount", 0);
			}else {
				list.getRecords().get(0).put("msgcount", allmsglist.size());
			}
		}
		log.debug("查看信息");
		return Result.success(list);
	}

	@GetMapping("/getMsgLimit")
	public Result<List<SystemNotify>> getLimitMsgAction() {
		List<SystemNotify> list = notifyHandlerService.findMsgLimit();
		if (list.size() > 0) {
		    return Result.success(list);
		} else {
			return null;
		}
	}

	@GetMapping("/getSubscription")
	public Result<List<SystemSubscription>> getUsersubscriptionAction() {
		UserInfo userInfo = UserInfoContext.get();
		List<SystemSubscription> list = notifyHandlerService.getSubsrciptionByUser(userInfo.getId());
		if (list.size() > 0) {
			return Result.success(list);
		} else {
			return null;
		}
	}
	@GetMapping("/pullMessage")
	public Result<?> pullMessageAction() {
		UserInfo userInfo = UserInfoContext.get();
		notifyHandlerService.pullMessage(userInfo);
		return Result.success();
	}
	

	@GetMapping("/getTarget")
	public Result<List<SystemTarget>> getTargetAction() {
		return Result.success(notifyHandlerService.findAllTarget());
	}

	@GetMapping("/findNitofyNums")
	public Result<List<Map<String, Object>>> findNitofyNumsAction() {
		UserInfo userInfo = UserInfoContext.get();
		List<Map<String, Object>> list = notifyHandlerService.findNotifyNums(userInfo.getId());
		return Result.success(list);
	}

	@GetMapping("/updateAllTargets")
	public Result<?> updateAllTargetsAction() {
		UserInfo userInfo = UserInfoContext.get();
		List<Map<String, Object>> list = notifyHandlerService.selectAllTargetsByUser(userInfo.getId());
		List<String> ids = new LinkedList<String>();
		for (Map<String, Object> map : list) {
			ids.add(map.get("notify").toString());
		}
		notifyHandlerService.read(userInfo, ids);
		return Result.success();
	}
	
 
	@GetMapping("/getMessageInfoById")
	public Object getInfoByIdAction(String id) {
	    String content = notifyHandlerService.findMessageTemplate( id);
		return content;
	}
	
	
	
	@PostMapping("/updateMessage")
	public Object updateAnnounceAction(String id,String content) {
		UserInfo userInfo = UserInfoContext.get();
		int result = notifyHandlerService.updateMessage(userInfo.getId(), content, id);
		return result;
	}
	

	@GetMapping("/findNoReadByUserAll")
	public Result<?>  findNoReadByUserAllAction() {
		UserInfo userInfo = UserInfoContext.get();
		List<SystemNotify> result = notifyHandlerService.findNoReadByUserAll(userInfo);
		return Result.success(result);
	}

}
