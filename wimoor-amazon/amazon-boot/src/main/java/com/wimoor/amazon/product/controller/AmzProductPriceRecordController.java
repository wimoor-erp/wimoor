package com.wimoor.amazon.product.controller;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.product.pojo.entity.AmzProductPriceRecord;
import com.wimoor.amazon.product.service.IAmzProductPriceRecordService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;

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
 * @since 2022-06-17
 */
@Api(tags = "产品自动调价改价记录接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/priceRecord")
public class AmzProductPriceRecordController {
	    final IAmzProductPriceRecordService iAmzProductPriceRecordService;
	    final AdminClientOneFeignManager adminClientOneFeignManager;
	    @ApiOperation(value = "获取产品类型")
	    @GetMapping("/getPriceRecord")
	    public Result<List<AmzProductPriceRecord>> getPriceRecordAction(String pid,String byday) {
	    	if(pid==null) {
	    		return Result.success(null);
	    	}
	    	List<AmzProductPriceRecord> list =null;
	    	if(StrUtil.isBlankOrUndefined(byday)) {
	    		  list = iAmzProductPriceRecordService.lambdaQuery().eq(AmzProductPriceRecord::getPid, pid).orderByDesc(AmzProductPriceRecord::getOpttime).list();
	    	}else {
	    		 list = iAmzProductPriceRecordService.lambdaQuery().eq(AmzProductPriceRecord::getPid, pid)
		    			.gt(AmzProductPriceRecord::getOpttime, byday)
		    			.orderByDesc(AmzProductPriceRecord::getOpttime).list();
		      
	    	}
	    	if(list!=null&&list.size()>0) {
	    		for(AmzProductPriceRecord item:list) {
	    			if(item.getLowestprice()==null) {
	    				item.setLowestprice(new BigDecimal("0"));
	    			}
	    			if(item.getPrice()==null) {
	    				item.setPrice(new BigDecimal("0"));
	    			}
	    			if(item.getShipprice()==null) {
	    				item.setShipprice(new BigDecimal("0"));
	    			}
	    			if(item.getOldprice()==null) {
	    				item.setOldprice(new BigDecimal("0"));
	    			}
	    			if(item.getOldshipprice()==null) {
	    				item.setOldshipprice(new BigDecimal("0"));
	    			}
	    			if(item.getRefprice()==null) {
	    				item.setRefprice(new BigDecimal("0"));
	    			}
	    			if(StrUtil.isNotBlank(item.getOperator())) {
	    				Result<UserInfo> result = adminClientOneFeignManager.getUserByUserId(item.getOperator());
		    			if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
		    		        UserInfo info = result.getData();
		    		        if(info.getUserinfo()!=null&&info.getUserinfo().get("name")!=null) {
		    		        	item.setOperator(info.getUserinfo().get("name").toString());
		    		        }
		    			}
	    			}
	    			
	    		}
	    	}
	    	  return Result.success(list);
	    }  
	    
}

