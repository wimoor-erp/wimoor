package com.wimoor.erp.change.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.change.pojo.entity.PurchaseFormEntryChangeReceive;
import com.wimoor.erp.change.service.IPurchaseFormEntryChangeReceiveService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Api(tags = "换货记录")
@RestController
@RequestMapping("/api/v1/purchase/purchaseFormEntryChangeReceive")
@RequiredArgsConstructor
public class PurchaseFormEntryChangeReceiveController {
	
	@Resource
	IPurchaseFormEntryChangeReceiveService purchaseFormEntryChangeReceiveService;
	
	
	@ApiOperation(value = "保存换货记录")
	@GetMapping("/saveRecord")
	@Transactional
	public Result<Map<String, Object>> saveRecordAction(String remark,String changeid,String amount) throws BizException {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> msgMap = new HashMap<String, Object>();
		PurchaseFormEntryChangeReceive purchaseChangeReceive = new PurchaseFormEntryChangeReceive();
		purchaseChangeReceive.setAmount(Integer.parseInt(amount));
		purchaseChangeReceive.setEntrychangeid(changeid);
		purchaseChangeReceive.setOperator(user.getId());
		purchaseChangeReceive.setOpttime(new Date());
		purchaseChangeReceive.setRemark(remark);
		if(StrUtil.isNotEmpty(remark)) {
			purchaseChangeReceive.setRemark(remark);
		}
		msgMap = purchaseFormEntryChangeReceiveService.saveMineAndinStock(purchaseChangeReceive, user);
		return Result.success(msgMap);
	}
	
	@ApiOperation(value = "撤销换货记录")
	@Transactional
	@GetMapping("/cancelInstock")
	public Result<String> cancelInstockAction(String receiveid) {
		UserInfo user = UserInfoContext.get();
		PurchaseFormEntryChangeReceive receive = purchaseFormEntryChangeReceiveService.getById(receiveid);
		Integer amount = receive.getAmount();
		if(amount>0) {
			//把此次的可用库存扣减掉 待入库加回来 
			purchaseFormEntryChangeReceiveService.cancelInstock(receive,user);
			return Result.success("操作成功");
		}else {
			return Result.success("操作失败");
		}
	}
	
	
}

