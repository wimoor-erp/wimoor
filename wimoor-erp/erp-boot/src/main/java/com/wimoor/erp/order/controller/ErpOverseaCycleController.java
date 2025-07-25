package com.wimoor.erp.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.api.AdminClientOneFeignManager;
import com.wimoor.erp.order.pojo.entity.ErpOverseaCycle;
import com.wimoor.erp.order.service.IErpOverseaCycleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/api/v1/OverseaCycle")
@RequiredArgsConstructor
public class ErpOverseaCycleController {
	final IErpOverseaCycleService iErpOverseaCycleService;
    final AdminClientOneFeignManager adminClientOneFeignManager;
	@ApiOperation("列表")
    @GetMapping("/list")
	public Result<List<ErpOverseaCycle>> getListData(String transtype){
	    UserInfo user = UserInfoContext.get();
	    LambdaQueryWrapper<ErpOverseaCycle> query=new LambdaQueryWrapper<ErpOverseaCycle>();
	    query.eq(ErpOverseaCycle::getShopid, user.getCompanyid());
	    query.eq(ErpOverseaCycle::getTranstype, transtype);
	    List<ErpOverseaCycle> list = iErpOverseaCycleService.list(query);
		Map<String,String> userNameMap = adminClientOneFeignManager.getAllUserName();
	    for(ErpOverseaCycle item:list) {
	    	try{
	    		if(userNameMap!=null) {
	    			item.setOperatorname(userNameMap.get(item.getOperator()));
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
	public Result<List<ErpOverseaCycle>> getListData(@RequestBody List<ErpOverseaCycle> list){
	    UserInfo user = UserInfoContext.get();
	    for(ErpOverseaCycle item:list) {
	    	item.setShopid(new BigInteger(user.getCompanyid()));
	    	item.setOperator(new BigInteger(user.getId()));
	    	item.setOpttime(new Date());
	    	LambdaQueryWrapper<ErpOverseaCycle> query=new LambdaQueryWrapper<ErpOverseaCycle>();
												   query.eq(ErpOverseaCycle::getShopid, user.getCompanyid());
												   query.eq(ErpOverseaCycle::getCountry, item.getCountry());
												   query.eq(ErpOverseaCycle::getTranstype, item.getTranstype());
			ErpOverseaCycle old = iErpOverseaCycleService.getOne(query);
		    if(item.getIsdefault()==true) {
		    	LambdaQueryWrapper<ErpOverseaCycle> queryDefault=new LambdaQueryWrapper<ErpOverseaCycle>();
			    	queryDefault.eq(ErpOverseaCycle::getShopid, user.getCompanyid());
			    	queryDefault.eq(ErpOverseaCycle::getCountry, item.getCountry());
			    	queryDefault.eq(ErpOverseaCycle::getIsdefault,true);
				ErpOverseaCycle olddefault = iErpOverseaCycleService.getOne(queryDefault);
                   if(olddefault!=null) {
                	   olddefault.setIsdefault(false);
					   iErpOverseaCycleService.updateById(olddefault);
                   }
		    }
		    if(old!=null) {
		    	item.setId(old.getId());
				iErpOverseaCycleService.updateById(item);
		    }else {
				iErpOverseaCycleService.save(item);
		    }
	    }
		return Result.success(list);
	}
}

