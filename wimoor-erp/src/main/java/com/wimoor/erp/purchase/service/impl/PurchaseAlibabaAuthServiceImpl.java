package com.wimoor.erp.purchase.service.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.client.policy.RequestPolicy;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.trade.param.AlibabaTradeGetBuyerViewParam;
import com.alibaba.trade.param.AlibabaTradeGetBuyerViewResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.HttpClientUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.purchase.mapper.PurchaseAlibabaAuthMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseAlibabaAuth;
import com.wimoor.erp.purchase.service.IPurchaseAlibabaAuthService;
import com.wimoor.erp.util.ApiCallService;
import com.wimoor.erp.util.CommonUtil;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
 

@Service("purchaseAlibabaAuthService")
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "alibaba")
public class PurchaseAlibabaAuthServiceImpl extends ServiceImpl<PurchaseAlibabaAuthMapper,PurchaseAlibabaAuth> implements IPurchaseAlibabaAuthService {
    
	@Setter
    String appKey;
    @Setter
    String appSecret;
    
	public static SimpleDateFormat alibabatimefmt = new SimpleDateFormat("yyyyMMddhhmmssSSS");

	public PurchaseAlibabaAuth saveAction(PurchaseAlibabaAuth purchaseAlibabaAuth) {
		QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		queryWrapper.eq("name", purchaseAlibabaAuth.getName());
		queryWrapper.eq("shopid", purchaseAlibabaAuth.getShopid());
		queryWrapper.eq("isDelete", 0);
		if(StrUtil.isBlank(purchaseAlibabaAuth.getAppkey())) {
			purchaseAlibabaAuth.setAppkey(appKey);
		}
		if(StrUtil.isBlank(purchaseAlibabaAuth.getAppsecret())) {
			purchaseAlibabaAuth.setAppsecret(appSecret);
		}
		PurchaseAlibabaAuth purchaseAlibabaAuth_old = this.getOne(queryWrapper);
		if (purchaseAlibabaAuth.idIsNULL()) {
			if (purchaseAlibabaAuth_old != null) {
				throw new ERPBizException("店铺名称重复!");
			}
			this.save(purchaseAlibabaAuth);
		} else {
			if (purchaseAlibabaAuth_old != null && !purchaseAlibabaAuth_old.getId().equals(purchaseAlibabaAuth.getId())) {
				throw new ERPBizException("店铺名称重复!");
			}else {
				purchaseAlibabaAuth_old=this.getById(purchaseAlibabaAuth.getId());
			}
			purchaseAlibabaAuth_old.setOperator(purchaseAlibabaAuth.getOperator());
			purchaseAlibabaAuth_old.setOpttime(purchaseAlibabaAuth.getOpttime());
			purchaseAlibabaAuth_old.setName(purchaseAlibabaAuth.getName());
			purchaseAlibabaAuth_old.setAppkey(purchaseAlibabaAuth.getAppkey());
			purchaseAlibabaAuth_old.setAppsecret(purchaseAlibabaAuth.getAppsecret());
			this.updateById(purchaseAlibabaAuth_old);
		}
		return purchaseAlibabaAuth;
	}

	public PurchaseAlibabaAuth refreshAuthToken(PurchaseAlibabaAuth purchaseAlibabaAuth) {
		if (new Date().after(purchaseAlibabaAuth.getRefreshTokenTimeout())) {
		   throw new ERPBizException("长效令牌失效，请到授权模块，1688绑定页面重新进行绑定。长效令牌时效半年");
		   //if(refreshAuthRefreshToken(purchaseAlibabaAuth)==null)return null;
		}
		String url = "https://gw.open.1688.com/openapi/param2/1/system.oauth2/getToken/" + purchaseAlibabaAuth.getAppkey();
		String contentType = "application/x-www-form-urlencoded";
		Map<String, String> props = new HashMap<String, String>();
		props.put("grant_type", "refresh_token");
		props.put("client_id", purchaseAlibabaAuth.getAppkey());
		props.put("client_secret", purchaseAlibabaAuth.getAppsecret());
		props.put("refresh_token", purchaseAlibabaAuth.getRefreshToken());
		props.put("Content-Type", contentType);
		String s = null;
		try {
			s = HttpClientUtil.postUrl(url, props, null, contentType);
		} catch (HttpException e) {
			return null;
		}
		if (GeneralUtil.isEmpty(s)) {
			return null;
		}
		JSONObject json = GeneralUtil.getJsonObject(s);
		String access_token = json.getString("access_token");
		String aliId = json.getString("aliId");
		String resource_owner = json.getString("resource_owner");
		int expires_in = json.getInteger("expires_in");
		String memberId = json.getString("memberId");
		purchaseAlibabaAuth.setAccessToken(access_token);
		purchaseAlibabaAuth.setAliId(new BigInteger(aliId));
		purchaseAlibabaAuth.setResourceOwner(resource_owner);
		Date afterDate = new Date(new Date().getTime() + (expires_in - 300) * 1000);
		purchaseAlibabaAuth.setAccessTokenTimeout(afterDate);
		purchaseAlibabaAuth.setMemberId(memberId);
		purchaseAlibabaAuth.setOpttime(new Date());
	    this.updateById(purchaseAlibabaAuth);
		return purchaseAlibabaAuth;
	}

	public PurchaseAlibabaAuth refreshAuthRefreshToken(PurchaseAlibabaAuth purchaseAlibabaAuth) {
		String url = "https://gw.open.1688.com/openapi/param2/1/system.oauth2/postponeToken/" + purchaseAlibabaAuth.getAppkey();
		url+="?access_token="+ purchaseAlibabaAuth.getAccessToken();
		url+="&client_id="+purchaseAlibabaAuth.getAppkey();
		url+="&client_secret="+ purchaseAlibabaAuth.getAppsecret();
		url+="&refresh_token="+purchaseAlibabaAuth.getRefreshToken();
		String s = null;
		try {
			s = HttpClientUtil.getUrl(url,null);
		} catch (HttpException e) {
			e.printStackTrace();
			return null;
		}
		if (GeneralUtil.isEmpty(s)) {
			return null;
		}
		JSONObject json = GeneralUtil.getJsonObject(s);
		String access_token = json.getString("access_token");
		String aliId = json.getString("aliId");
		String refresh_token = json.getString("refresh_token");
		String resource_owner = json.getString("resource_owner");
		int expires_in = json.getInteger("expires_in");
		String refresh_token_timeout = json.getString("refresh_token_timeout");
		String memberId = json.getString("memberId");
		purchaseAlibabaAuth.setAccessToken(access_token);
		purchaseAlibabaAuth.setRefreshToken(refresh_token);
		purchaseAlibabaAuth.setAliId(new BigInteger(aliId));
		purchaseAlibabaAuth.setResourceOwner(resource_owner);
		Date afterDate = new Date(new Date().getTime() + (expires_in - 300) * 1000);
		purchaseAlibabaAuth.setAccessTokenTimeout(afterDate);
		purchaseAlibabaAuth.setMemberId(memberId);
		purchaseAlibabaAuth.setOpttime(new Date());
		try {
			Date refreshtime = alibabatimefmt.parse(refresh_token_timeout);
			purchaseAlibabaAuth.setRefreshTokenTimeout(refreshtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.baseMapper.updateById(purchaseAlibabaAuth);
		return purchaseAlibabaAuth;
	}

	public PurchaseAlibabaAuth bindAuth(UserInfo user, String code, String state) {
		PurchaseAlibabaAuth nowPurchaseAlibabaAuth = super.getById(state);
		String url = "https://gw.open.1688.com/openapi/http/1/system.oauth2/getToken/" + nowPurchaseAlibabaAuth.getAppkey();
		url+="?grant_type=authorization_code";
		url+="&need_refresh_token=true";
		url+="&code="+ code;
		url+="&client_id="+nowPurchaseAlibabaAuth.getAppkey();
		url+="&client_secret="+ nowPurchaseAlibabaAuth.getAppsecret();
		url+="&redirect_uri=http://localhost:12315";
		String s = "";
		PurchaseAlibabaAuth purchaseAlibabaAuth = null;
		try {
			s = HttpClientUtil.getUrl(url,null);
			JSONObject json = GeneralUtil.getJsonObject(s);
			String access_token = json.getString("access_token");
			String aliId = json.getString("aliId");
			String refresh_token = json.getString("refresh_token");
			String resource_owner = json.getString("resource_owner");
			int expires_in = json.getInteger("expires_in");
			String refresh_token_timeout = json.getString("refresh_token_timeout");
			String memberId = json.getString("memberId");
			Date afterDate = new Date(new Date().getTime() + (expires_in - 300) * 1000);
			QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		 
			queryWrapper.eq("aliId", aliId);
			queryWrapper.eq("shopid",user.getCompanyid());
			queryWrapper.eq("memberId", memberId);
			queryWrapper.eq("resource_owner", resource_owner);
			queryWrapper.eq("refresh_token", refresh_token);
			try {
				purchaseAlibabaAuth = this.getOne(queryWrapper);
			}catch(Exception e) {
				e.printStackTrace();
			}
			if (purchaseAlibabaAuth != null) {
				if (!purchaseAlibabaAuth.isDelete()) {
					throw new ERPBizException("该账户已经绑定！");
				}
				purchaseAlibabaAuth.setName(nowPurchaseAlibabaAuth.getName());
				purchaseAlibabaAuth.setDelete(false);
				if (GeneralUtil.isNotEmpty(nowPurchaseAlibabaAuth.getAppkey())) {
					purchaseAlibabaAuth.setAppkey(nowPurchaseAlibabaAuth.getAppkey());
					purchaseAlibabaAuth.setAppsecret(nowPurchaseAlibabaAuth.getAppsecret());
				}
				this.removeById(nowPurchaseAlibabaAuth.getId());
			} else {
				purchaseAlibabaAuth = nowPurchaseAlibabaAuth;
			}
			purchaseAlibabaAuth.setAccessToken(access_token);
			purchaseAlibabaAuth.setRefreshToken(refresh_token);
			purchaseAlibabaAuth.setAliId(new BigInteger(aliId));
			purchaseAlibabaAuth.setResourceOwner(resource_owner);
			purchaseAlibabaAuth.setMemberId(memberId);
			purchaseAlibabaAuth.setAccessTokenTimeout(afterDate);
			purchaseAlibabaAuth.setOpttime(new Date());
			try {
				Date refreshtime = alibabatimefmt.parse(refresh_token_timeout);
				purchaseAlibabaAuth.setRefreshTokenTimeout(refreshtime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.baseMapper.updateById(purchaseAlibabaAuth);
		} catch (HttpException e) {
			e.printStackTrace();
		}
		return purchaseAlibabaAuth;
	}

	public List<PurchaseAlibabaAuth> getAuthData(UserInfo user) {
		QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		queryWrapper.eq("shopid", user.getCompanyid());
		queryWrapper.eq("isDelete", 0);
		return  this.list(queryWrapper);
	}

	public static ApiExecutor getApiExecutor(PurchaseAlibabaAuth alibabaAuth) {
		return new ApiExecutor(alibabaAuth.getAppkey(), alibabaAuth.getAppsecret());
	}

	// 获取物流的跟踪信息
	public String captureLogisticsTraceInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid) {
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		if (alibabaAuth != null && alibabaAuth.getAccessTokenTimeout() != null && new Date().after(alibabaAuth.getAccessTokenTimeout())) {
			if(this.refreshAuthToken(alibabaAuth)==null)return null;
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("orderId", alibabaOrderid);
		if(alibabaAuth==null) {
			return null;
		}
		param.put("access_token", alibabaAuth.getAccessToken());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.logistics",
				"alibaba.trade.getLogisticsTraceInfo.buyerView", 1, "param2", alibabaAuth.getAppkey());
		String result = ApiCallService.callApi(urlPath, alibabaAuth.getAppsecret(), param);
		JSONObject json = GeneralUtil.getJsonObject(result);
		if (json.get("errorMessage") != null) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				return null;
			}
		}
		return result;
	}

	public String captureLogisticsInfo(UserInfo user, String alibabaAuthid, String alibabaOrderid) {
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		if (alibabaAuth.getAccessTokenTimeout() != null && new Date().after(alibabaAuth.getAccessTokenTimeout())) {
			if(this.refreshAuthToken(alibabaAuth)==null)return null;
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("webSite", "1688");
		param.put("orderId", alibabaOrderid);
		param.put("access_token", alibabaAuth.getAccessToken());
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.logistics",
				"alibaba.trade.getLogisticsInfos.buyerView", 1, "param2", alibabaAuth.getAppkey());
		String result = ApiCallService.callApi(urlPath, alibabaAuth.getAppsecret(), param);
		JSONObject json = GeneralUtil.getJsonObject(result);
		if (json.get("errorMessage") != null) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				return null;
			}
		}
		return result;
	}

	public AlibabaTradeGetBuyerViewResult captureOrderFromAlibaba(UserInfo user, String alibabaAuthid, String alibabaOrderid) {
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		if (alibabaAuth == null) {
			return null;
		}
		if(alibabaAuth.isDelete()==false) {
			if (alibabaAuth.getAccessTokenTimeout() != null && new Date().after(alibabaAuth.getAccessTokenTimeout())) {
				if(this.refreshAuthToken(alibabaAuth)==null)return null;
			}
			ApiExecutor apiExecutor = PurchaseAlibabaAuthServiceImpl.getApiExecutor(alibabaAuth);
			AlibabaTradeGetBuyerViewParam param = new AlibabaTradeGetBuyerViewParam();
			APIId apiId = new APIId("com.alibaba.trade", "alibaba.trade.get.buyerView", 1);
			param.setOceanApiId(apiId);
			RequestPolicy oceanRequestPolicy = new RequestPolicy();
			param.setOceanRequestPolicy(oceanRequestPolicy);
			param.setWebSite("1688");
			param.setOrderId(alibabaOrderid);
			SDKResult<AlibabaTradeGetBuyerViewResult> result = apiExecutor.execute(param, alibabaAuth.getAccessToken());
			if (result.getResult() != null) {
				if (GeneralUtil.isNotEmpty(result.getResult().getErrorCode())) {
					throw new ERPBizException(result.getResult().getErrorMessage());
				}
				return result.getResult();
			} else {
				throw new ERPBizException(result.getErrorMessage());
			}
		}else {
			return null; 
		}

	}

	public String captureRefundByOrderId(UserInfo user, Map<String, String> map) {
		if (map == null || map.size() < 0) {
			return null;
		}
		String alibabaAuthid = map.get("alibabaAuthid");
		String alibabaOrderid = map.get("alibabaOrderid");
		String queryType = map.get("queryType");
		String dipsuteType = map.get("dipsuteType");
		PurchaseAlibabaAuth alibabaAuth = this.getById(alibabaAuthid);
		if (alibabaAuth.getAccessTokenTimeout() != null && new Date().after(alibabaAuth.getAccessTokenTimeout())) {
			if(this.refreshAuthToken(alibabaAuth)==null)return null;
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("orderId", alibabaOrderid);
		param.put("queryType", queryType);
		param.put("dipsuteType", dipsuteType);
		String urlPath = CommonUtil.buildInvokeUrlPath("com.alibaba.trade",
				"alibaba.trade.refund.OpQueryBatchRefundByOrderIdAndStatus", 1, "param2", alibabaAuth.getAppkey());
		String result = ApiCallService.callApi(urlPath, alibabaAuth.getAppsecret(), param);
		JSONObject json = GeneralUtil.getJsonObject(result);
		if ("order.find.error".equals(json.get("errorCode"))) {
			Object errorMsg = json.get("errorMessage");
			if (errorMsg != null) {
				throw new ERPBizException(errorMsg.toString());
			}
		}
		return result;
	}

	public PurchaseAlibabaAuth updateAlibaba(UserInfo user, String id) {
		PurchaseAlibabaAuth purchaseAlibabaAuth = super.getById(id);
		if (purchaseAlibabaAuth != null) {
			purchaseAlibabaAuth.setDelete(true);
			purchaseAlibabaAuth.setOpttime(new Date());
			purchaseAlibabaAuth.setOperator(user.getId());
			this.updateById(purchaseAlibabaAuth);
		} else {
			throw new ERPBizException("请选择正确的账号解绑！");
		}
		return purchaseAlibabaAuth;
	}

	@Override
	public PurchaseAlibabaAuth selectBymemberId(String memberid) {
		QueryWrapper<PurchaseAlibabaAuth> queryWrapper=new QueryWrapper<PurchaseAlibabaAuth>();
		queryWrapper.eq("memberId", memberid);
		List<PurchaseAlibabaAuth> list = this.baseMapper.selectList(queryWrapper);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}

	public String getAppKey(String id) {
		PurchaseAlibabaAuth auth = this.baseMapper.selectById(id);
		if(StrUtil.isBlank(auth.getAppkey())) {
			return appKey;
		}else {
			return auth.getAppkey();
		}
	}
 

}
