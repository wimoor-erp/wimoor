package com.wimoor.amazon.auth.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.IWareHouseFBAService;
import com.wimoor.amazon.summary.service.ISummaryOrderReportService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
 

@Api(tags = "站点接口")
@RestController
@RequestMapping("/api/v1/amzmarketplace")
//@Slf4j
@RequiredArgsConstructor
public class MarketplaceController {
	
	final IMarketplaceService iMarketplaceService;
	final IWareHouseFBAService wareHouseFBAService;
	final ISummaryOrderReportService iSummaryOrderReportService;
	
    @ApiOperation(value = "通过名称获取站点")
    @GetMapping("/getByName")
    @Cacheable(value = "marketplaceCache")
    public Result<Marketplace> getMarketplaceAction(@RequestParam String name) {
    	QueryWrapper<Marketplace> query=new QueryWrapper<Marketplace>();
    	query.eq("name", name);
		List<Marketplace> result = iMarketplaceService.list(query);
		if(result.size()>0) {
			 return Result.success(result.get(0));
		}else {
			return Result.success(null);
		}
    }
    
    @ApiOperation(value = "通过国家获取站点")
    @GetMapping("/getByCountry")
    @Cacheable(value = "marketplaceCache")
    public Result<Marketplace> getMarketplaceByCountryAction(@RequestParam String country) {
    	QueryWrapper<Marketplace> query=new QueryWrapper<Marketplace>();
    	query.eq("market", country);
		List<Marketplace> result = iMarketplaceService.list(query);
		if(result.size()>0) {
			 return Result.success(result.get(0));
		}else {
			return Result.success(null);
		}
    }   
    
    @ApiOperation(value = "通过SKU获取站点")
    @GetMapping("/getBySku")
    public Result<List<Marketplace>> getMarketplaceAction(@RequestParam String sku,@RequestParam String groupid) {
		List<Marketplace> result = iMarketplaceService.getMarketPointBySkuGroup(groupid,sku);
		if(result.size()>0) {
			 return Result.success(result);
		}else {
			return Result.success(null);
		}
        
    }    

    @ApiOperation(value = "通过MSKU获取站点")
    @GetMapping("/getByMSku")
    public Result<List<Marketplace>> getMarketplaceByMSkuAction(@RequestParam String msku,@RequestParam String groupid) {
    	UserInfo userinfo = UserInfoContext.get();
    	if(StrUtil.isBlank(groupid)) {
    		groupid=null;
    	}
		List<Marketplace> result = iMarketplaceService.getMarketPointByMSku(userinfo.getCompanyid(),groupid,msku);
		if(result.size()>0) {
			 return Result.success(result);
		}else {
			return Result.success(null);
		}
        
    }  
    
    @ApiOperation(value = "通过SKU获取站点")
    @GetMapping("/refreshFbaWarehouse")
    public Result<?> refreshFbaWarehouseAction() {
    	Set<String> shopset = iSummaryOrderReportService.getAvailableAmazonShop();
    	for(String shopid:shopset) {
    		  wareHouseFBAService.refreshFBA(shopid,null);
    	}
        return Result.judge(true);
    }  
}
