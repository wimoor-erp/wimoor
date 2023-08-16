package com.wimoor.amazon.auth.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.pojo.vo.AmazonGroupVO;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.auth.service.IWareHouseFBAService;
import com.wimoor.amazon.report.pojo.entity.AmazonAuthMarketPerformance;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
 

@Api(tags = "授权接口")
@RestController
@RequestMapping("/api/v1/amzauthority")
@Component("amazonAuthorityController")
//@Slf4j
@RequiredArgsConstructor
public class AmazonAuthorityController {
	
	 private final IAmazonAuthorityService iAmazonAuthorityService;
	 private final IAmazonGroupService iAmazonGroupService;
	 private final IMarketplaceService iMarketplaceService;
	 private final IWareHouseFBAService wareHouseFBAService;

	 
	    @ApiOperation(value = "获取店铺信息，格式:[authid-marketplaceid]")
	    @GetMapping("/getAuthMaketplace")
	    public Result<?> selectAuthMaketplaceAction() {
	    	UserInfo userinfo = UserInfoContext.get();
	    	List<Map<String, Object>> result = iAmazonAuthorityService.selectAuthMaketplace(userinfo.getCompanyid());
	        return Result.success(result);
	    }
	 	
	 
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
	    
	    @ApiOperation(value = "获取区域")
	    @GetMapping("/getRegionByGroup")
	    public Result<List<AmazonAuthority>> getRegionByGroupAction(String groupid) {
	    	List<AmazonAuthority> mylist = iAmazonAuthorityService.selectByGroupId(groupid);
	    	List<AmazonAuthority> result=new LinkedList<AmazonAuthority>();
	    	for(AmazonAuthority item:mylist) {
	    		AmazonAuthority auth = new AmazonAuthority();
	    		auth.setId(item.getId());
	    		auth.setRegion(item.getRegion());
	    		auth.setGroupid(item.getGroupid());
	    		auth.setSellerid(auth.getSellerid());
	    		result.add(auth);
	    	}
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "获取站点")
	    @GetMapping("/getPerformance")
	    public Result<AmazonAuthMarketPerformance> getPerformanceAction(String groupid,String marketplaceid) {
	    	AmazonAuthority auth = iAmazonAuthorityService.selectByGroupAndMarket(groupid, marketplaceid);
	    	AmazonAuthMarketPerformance result = iAmazonAuthorityService.getPerformance(auth,marketplaceid);
	    	if(result!=null&&result.getPerformance()!=null) {
	    		result.setPerformanceJson(GeneralUtil.getJsonObject(result.getPerformance()));
	    	}
	        return Result.success(result);
	    }
	    
	    @ApiOperation(value = "刷新旧的WMS店铺授权转移到SP-API")
	    @GetMapping("/getRefreshToken")
	    public Result<?> getRefreshTokenAction() {
	    	iAmazonAuthorityService.getRefreshTokenByRegion("EU");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("NA");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("JP");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("IN");
	    	iAmazonAuthorityService.getRefreshTokenByRegion("AU");
	      	iAmazonAuthorityService.getRefreshTokenByRegion("SG");
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
	    		wareHouseFBAService.refreshFBA(result.getShopId(), null);
	    		result.setMwsauthtoken("****");
	    		result.setRefreshToken("****");
	    		return Result.success(result);
	    	}else {
	    		return Result.failed("授权失败请重试");
	    	}
	    }
	    
	    @ApiOperation(value = "获取店铺")
	    @PostMapping("/saveAuth")
	    public Result<AmazonAuthority> saveAuthAction(@RequestBody AmazonAuthority auth) {
	    	UserInfo userinfo = UserInfoContext.get();
	    	auth.setShopId(userinfo.getCompanyid());
			AmazonAuthority result = iAmazonAuthorityService.saveAuth(auth) ;
	    	if(result!=null) {
	    		wareHouseFBAService.refreshFBA(result.getShopId(), null);
	    		result.setMwsauthtoken("****");
	    		result.setRefreshToken("****");
	    		return Result.success(result);
	    	}else {
	    		return Result.failed("授权失败请重试");
	    	}
	    }
	    
	    @ApiOperation(value = "获取已经授权的店铺")
	    @GetMapping("/getBindSeller")
	    public Result<List<AmazonGroupVO>> getBindSellerAction() {
	    	UserInfo userinfo = UserInfoContext.get();
	    	try {
	    		List<AmazonGroupVO>  result = iAmazonAuthorityService.selectBindAuth(userinfo);
	    		for(AmazonGroupVO item:result) {
	    			item.convertRegionsToItem();
	    		}
	    		return Result.success(result);
	    	}catch(Exception e) {
    			e.printStackTrace();
    			throw new BizException(e.getMessage());
	    	}
	    }
	    
	    @ApiOperation(value = "解除绑定授权的店铺")
	    @GetMapping("/deleteByLogic")
	    public Result<String> deleteByLogicAction(@ApiParam("sellerid")@RequestParam String sellerid) {
	    	UserInfo userinfo = UserInfoContext.get();
	    	Map<String,Object> param=new HashMap<String, Object>();
	    	param.put("shopid", userinfo.getCompanyid());
	    	param.put("sellerid",sellerid);
	    	int result = iAmazonAuthorityService.deleteByLogic(param);
	    	String msg="";
	    	if(result>0) {
	    		msg = "解除成功！";
	    	}else {
	    		msg = "解除失败！";
	    	}
	        return Result.success(msg);
	    }
	    

	    
}
