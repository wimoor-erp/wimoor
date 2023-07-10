package com.wimoor.erp.purchase.alibaba.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.purchase.alibaba.pojo.entity.Open1688Message;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseAlibabaAuth;
import com.wimoor.erp.purchase.alibaba.service.IOpen1688MessageService;
import com.wimoor.erp.purchase.alibaba.service.IPurchaseAlibabaAuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
@Api(tags = "采购1688接口")
@RestController
@RequestMapping("/api/v1/purchase1688")
@RequiredArgsConstructor
public class AlibabaController {
	
	
	final IPurchaseAlibabaAuthService purchaseAlibabaAuthService;
    final IOpen1688MessageService iOpen1688MessageService;
	
	@ApiOperation("绑定1688账号")
	@PostMapping("/submitname")
	public Result<PurchaseAlibabaAuth> submitNameAction(@ApiParam("授权对象")@RequestBody PurchaseAlibabaAuth purchaseAlibabaAuth)  {
		UserInfo user = UserInfoContext.get();
		purchaseAlibabaAuth.setShopid(user.getCompanyid());
		purchaseAlibabaAuth.setCreatetime(new Date());
		purchaseAlibabaAuth.setCreator(user.getId());
		purchaseAlibabaAuth.setOperator(user.getId());
		purchaseAlibabaAuth.setOpttime(new Date());
		return Result.success(purchaseAlibabaAuthService.saveAction(purchaseAlibabaAuth));
	}
 
	
	@ApiOperation("绑定1688账号")
	@GetMapping("/bindAuthData")
	public Result<PurchaseAlibabaAuth> bindAuthDataAction(
			@ApiParam("账号code")@RequestParam String code,
			@ApiParam("账号状态")@RequestParam String state) {
		UserInfo user = UserInfoContext.get();
		return Result.success(purchaseAlibabaAuthService.bindAuth(user,code,state));
	}
	
	@ApiOperation("刷新1688授权")
	@GetMapping("/refreshAuthData")
	public Result<PurchaseAlibabaAuth> refreshAuthDataAction(@ApiParam("账号授权ID")@RequestParam String id) {
		UserInfo user = UserInfoContext.get();
		PurchaseAlibabaAuth auth = purchaseAlibabaAuthService.getById(id);
		if(user!=null) {
			auth.setOperator(user.getId());
		}
		return Result.success(purchaseAlibabaAuthService.refreshAuthToken(auth));
	}
	
	 
	@ApiOperation("获取当前绑定的1688账号列表")
	@GetMapping("/getAuthData")
	public Result<List<PurchaseAlibabaAuth>> getAuthDataAction()  {
		UserInfo user = UserInfoContext.get();
		return Result.success(purchaseAlibabaAuthService.getAuthData(user));
	}
	@ApiOperation("获取当前绑定的1688账号列表")
	@GetMapping("/getAddress")
	public Result<Object> getAddressAction(@ApiParam("账号授权ID")@RequestParam String id)  {
		return Result.success(purchaseAlibabaAuthService.getAddress(id));
	}
	@GetMapping("/productInfo")
	public Result<JSONObject> productInfoAction(String alibabaAuthid,
			String productId ) throws BizException {
		JSONObject result = purchaseAlibabaAuthService.productInfo(alibabaAuthid,productId);
		return Result.success(result);
	}
	
	@ApiOperation("删除并解绑1688账号")
	@GetMapping("/delete")
	public Result<List<PurchaseAlibabaAuth>> deleteAction(@ApiParam("账号授权ID")@RequestParam String id) {
		UserInfo user = UserInfoContext.get();
		if(id!=null) {
			purchaseAlibabaAuthService.updateAlibaba(user, id);
		}
		return Result.success(purchaseAlibabaAuthService.getAuthData(user));
	}
	
	@ApiOperation("获取1688绑定链接")
	@GetMapping("/get1688Url")
	public Result<String> getBindAuthUrlAction(
			@ApiParam("跳转路径URL")@RequestParam String redirectUrl,
			@ApiParam("账号ID")@RequestParam String id
			) {
		String url=
		"https://auth.1688.com/oauth/authorize?site=1688&client_id="
		+ purchaseAlibabaAuthService.getAppKey(id)
		+ "&redirect_uri="
		+ purchaseAlibabaAuthService.getJuShiTa()
		+ "&state=" + id +"@"+redirectUrl+ "&view=web";
		return Result.success(url);
	}
	
	
	@ApiOperation("1688message")
	@RequestMapping("/message")
	public String messageApiAction(HttpServletRequest request,HttpServletResponse response, Model model) {
		Open1688Message message=new Open1688Message();
		message.setSignature(request.getParameter("_aop_signature"));
        message.setContent(request.getParameter("message"));		
		message.setOpttime(new Date());
		iOpen1688MessageService.saveOrUpdate(message);
		return "success";
	}

	
}
