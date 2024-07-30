package com.wimoor.amazon.inbound.controller;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.inbound.pojo.entity.AmzInboundFbaCycle;
import com.wimoor.amazon.inbound.service.IAmzInboundFbaCycleService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Api(tags = "头程配置")
@RestController
@SystemControllerLog("发货地址模块")
@RequestMapping("/api/v1/amzInboundFbaCycle")
@RequiredArgsConstructor
public class AmzInboundFbaCycleController {
	final IAmzInboundFbaCycleService iAmzInboundFbaCycleService;
	final AdminClientOneFeignManager adminClientOneFeign;
	@ApiOperation("列表")
    @GetMapping("/list")
	public Result<List<AmzInboundFbaCycle>> getListData(String transtype){
	    UserInfo user = UserInfoContext.get();
	    LambdaQueryWrapper<AmzInboundFbaCycle> query=new LambdaQueryWrapper<AmzInboundFbaCycle>();
	    query.eq(AmzInboundFbaCycle::getShopid, user.getCompanyid());
	    query.eq(AmzInboundFbaCycle::getTranstype, transtype);
	    List<AmzInboundFbaCycle> list = iAmzInboundFbaCycleService.list(query);
	    for(AmzInboundFbaCycle item:list) {
	    	try{
	    		Result<UserInfo> optResult = adminClientOneFeign.getUserByUserId(item.getOperator().toString());
	    		if(Result.isSuccess(optResult)&&optResult.getData()!=null) {
	    			item.setOperatorname(optResult.getData().getUserinfo().get("name").toString());
	    		}
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    }
		return Result.success(list);
	}
	
	@ApiOperation("保存")
    @PostMapping("/save")
	@Transactional
	@CacheEvict(value = { "FbaCycle" }, allEntries = true)
	public Result<List<AmzInboundFbaCycle>> getListData(@RequestBody List<AmzInboundFbaCycle> list){
	    UserInfo user = UserInfoContext.get();
	    for(AmzInboundFbaCycle item:list) {
	    	item.setShopid(new BigInteger(user.getCompanyid()));
	    	item.setOperator(new BigInteger(user.getId()));
	    	item.setOpttime(new Date());
	    	LambdaQueryWrapper<AmzInboundFbaCycle> query=new LambdaQueryWrapper<AmzInboundFbaCycle>();
												   query.eq(AmzInboundFbaCycle::getShopid, user.getCompanyid());
												   query.eq(AmzInboundFbaCycle::getMarketplaceid, item.getMarketplaceid());
												   query.eq(AmzInboundFbaCycle::getTranstype, item.getTranstype());
		    AmzInboundFbaCycle old = iAmzInboundFbaCycleService.getOne(query);
		    if(item.getIsdefault()==true) {
		    	LambdaQueryWrapper<AmzInboundFbaCycle> queryDefault=new LambdaQueryWrapper<AmzInboundFbaCycle>();
			    	queryDefault.eq(AmzInboundFbaCycle::getShopid, user.getCompanyid());
			    	queryDefault.eq(AmzInboundFbaCycle::getMarketplaceid, item.getMarketplaceid());
			    	queryDefault.eq(AmzInboundFbaCycle::getIsdefault,true);
                   AmzInboundFbaCycle olddefault = iAmzInboundFbaCycleService.getOne(queryDefault);
                   if(olddefault!=null) {
                	   olddefault.setIsdefault(false);
                	   iAmzInboundFbaCycleService.updateById(olddefault);
                   }
		    }
		    if(old!=null) {
		    	item.setId(old.getId());
		    	iAmzInboundFbaCycleService.updateById(item);
		    }else {
		    	iAmzInboundFbaCycleService.save(item); 
		    }
	    }
		return Result.success(list);
	}
}

