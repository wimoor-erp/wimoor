package com.wimoor.erp.stock.controller;


import java.math.BigInteger;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaTrans;
import com.wimoor.erp.stock.service.IErpDispatchOverseaTransService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Api(tags = "海外仓调库单接口")
@RestController
@RequestMapping("/api/v1/inventory/dispatch/overseaTrans")
@RequiredArgsConstructor
public class ErpDispatchOverseaTransController {
	@Resource
	IErpDispatchOverseaTransService iErpDispatchOverseaTransService;
	    
    @Transactional
	@GetMapping("/getInfo")
	public Result<ErpDispatchOverseaTrans> getInfoAction(String id){
		return  Result.success(iErpDispatchOverseaTransService.lambdaQuery().eq(ErpDispatchOverseaTrans::getFormid,id).one());
	}
    
    @Transactional
  	@PostMapping("/saveInfo")
  	public Result<Boolean> saveInfoAction(@RequestBody ErpDispatchOverseaTrans trans){
    	UserInfo user = UserInfoContext.get();
    	ErpDispatchOverseaTrans one=iErpDispatchOverseaTransService.lambdaQuery().eq(ErpDispatchOverseaTrans::getFormid,trans.getFormid()).one();
    	if(one==null) {
    		trans.setOperator(new BigInteger(user.getId()));
    		trans.setOpttime(new Date());
    		return  Result.success(iErpDispatchOverseaTransService.save(trans));
    	}else {
    		trans.setId(one.getId());
    		trans.setOperator(new BigInteger(user.getId()));
    		trans.setOpttime(new Date());
    		return  Result.success(iErpDispatchOverseaTransService.updateById(trans));
    	}
  		
  	}
	
}

