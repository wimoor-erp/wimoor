package com.wimoor.amazon.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
 

@Api(tags = "授权接口")
@RestController
@RequestMapping("/api/v1/amzauthority")
//@Slf4j
@RequiredArgsConstructor
public class AmazonAuthorityController {
	
	 private final IAmazonAuthorityService iAmazonAuthorityService;
	 private final IAmazonGroupService iAmazonGroupService;
	 private final IMarketplaceService iMarketplaceService;


	    @ApiOperation(value = "获取店铺")
	    @GetMapping("/getAmazonGroup")
	    public Result<List<AmazonGroup>> getAmazonGroupAction() {
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<AmazonGroup> result = iAmazonGroupService.getGroupByUser(userinfo);
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "获取所有站点")
	    @GetMapping("/getMarketAll")
	    public Result<List<Marketplace>> getMarketplaceAction() {
	    	List<Marketplace> result = iMarketplaceService.findAllMarketplace();
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "获取所有站点")
	    @GetMapping("/getMarketBind")
	    public Result<List<Marketplace>> getMarketBindAction() {
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<Marketplace> result = iMarketplaceService.findByShopid(userinfo.getCompanyid());
	        return Result.success(result);
	    }
	    
	    
	    @ApiOperation(value = "获取站点")
	    @GetMapping("/getMarketBySeller")
	    public Result<List<Marketplace>> getMarketplaceBySellerAction(String sellerid) {
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<Marketplace> result = iMarketplaceService.selectMarketBySellerid(sellerid,userinfo.getCompanyid());
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "获取站点")
	    @GetMapping("/getMarketByGroup")
	    public Result<List<Marketplace>> getMarketByGroupAction(String groupid) {
	    	List<Marketplace> result = iMarketplaceService.findMarketplaceByGroup(groupid);
	    	if(StrUtil.isEmptyIfStr(groupid)) {
	    		return getMarketBindAction();
	    	}else {
	    		return Result.success(result);
	    	}
	    }
	    
	    @ApiOperation(value = "刷新旧的WMS店铺授权转移到SP-API")
	    @GetMapping("/getRefreshToken")
	    public Result<?> getRefreshTokenByRegionAction() {
			//String objectName = "amz/logo/exampleobject2.txt";
			//oSSApiService.putObject("wimoor-file",objectName, new ByteArrayInputStream("testAPi".getBytes()) );
	    	iAmazonAuthorityService.getRefreshTokenByRegion("EU");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("NA");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("JP");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("IN");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("AU");
	        return Result.judge(true);
	    }
	    
	    @ApiOperation(value = "获取店铺对应的授权链接")
	    @GetMapping("/getAuthUrl")
	    public Result<String> getUrlAction(String groupid,String marketplaceid) {
	    	String result=iAmazonAuthorityService.getAuthUrl(groupid,marketplaceid);
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "获取店铺")
	    @GetMapping("/authSeller")
	    public Result<AmazonAuthority> authSellerAction(String state,String selling_partner_id,String mws_auth_token,String spapi_oauth_code) {
	    	AmazonAuthority result = iAmazonAuthorityService.authSeller(state,selling_partner_id,mws_auth_token,spapi_oauth_code);
	    	if(result!=null) {
	    		return Result.success(result);
	    	}else {
	    		return Result.failed("授权失败请重试");
	    	}
	    }
	    
	    
	    
}
